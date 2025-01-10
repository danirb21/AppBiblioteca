package com.campuscamara.app.model;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BbddHandler {

    private DatabaseReference databaseReference;
    private DataSnapshot dataSnapshot;
    private FirebaseAuth mAuth;

    public BbddHandler(String url) {
        this.setDatabaseReference(FirebaseDatabase.getInstance(url).getReference());
    }

    public void setConexion(String url) {
        this.setDatabaseReference(FirebaseDatabase.getInstance(url).getReference());
    }

    public boolean newUser(Usuario usuario) throws Exception {
        mAuth = FirebaseAuth.getInstance(this.databaseReference.getDatabase().getApp());
        try {
            // Crear usuario
            Task<AuthResult> createUserTask = mAuth.createUserWithEmailAndPassword(usuario.getUsername(), usuario.getPassword());
            Tasks.await(createUserTask);
            if (!createUserTask.isSuccessful()) {
                throw createUserTask.getException();
            }

            // Iniciar sesión con el nuevo usuario
            Task<AuthResult> signInTask = mAuth.signInWithEmailAndPassword(usuario.getUsername(), usuario.getPassword());
            Tasks.await(signInTask);
            if (!signInTask.isSuccessful()) {
                throw signInTask.getException();
            }

            // Obtener usuario actual y setear UID
            Usuario user = usuario;
            if (mAuth.getCurrentUser() != null) {
                // Guardar el usuario en la base de datos
                Task<Void> setValueTask = this.getDatabaseReference().child("usuarios").child(mAuth.getCurrentUser().getUid()).setValue(user);
                Tasks.await(setValueTask);
                if (!setValueTask.isSuccessful()) {
                    throw setValueTask.getException();
                }

                mAuth.signOut();
                return true;
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkuser(Usuario usuario) {
      /*  this.dataSnapshot=this.databaseReference.child("usuarios").orderByChild("username").equalTo(usuario.getUsername()).orderByChild("password").equalTo(usuario.getPassword())
                .orderByChild("esPersonal").equalTo(usuario.isEsPersonal()).get().getResult();
        return dataSnapshot.exists();

       */
        boolean bol;
        mAuth = FirebaseAuth.getInstance();
        bol = true;
        //if(mAuth.signInWithEmailAndPassword(usuario.getUsername(), usuario.getPassword()).isSuccessful()){
        //bol=true;
        try {
            Tasks.await(mAuth.signInWithEmailAndPassword(usuario.getUsername(), usuario.getPassword()));
        } catch (ExecutionException e) {
            bol = false;
        } catch (InterruptedException e) {
            bol = false;
        }
        return bol;
    }

    public <T> List<T> buscarPorCampo(Class<T> tClass, String key, String valor) throws ExecutionException, InterruptedException {
        Task<DataSnapshot> task = this.getDatabaseReference().child("libros").orderByChild(key).equalTo(valor).get();
        Tasks.await(task);
        dataSnapshot = task.getResult();
        List<T> lista;
        if (this.dataSnapshot.getChildrenCount() > 1) {
            GenericTypeIndicator<List<T>> t = new GenericTypeIndicator<List<T>>() {
            };
            lista = dataSnapshot.getValue(t);
        } else {
            lista = new ArrayList<>();
            lista.add(this.dataSnapshot.getValue(tClass));
        }
        return lista;
    }
    public Libro buscarporId(String id) throws ExecutionException, InterruptedException {
        Task<DataSnapshot> task=this.databaseReference.child("libros").child(id).get();
        Tasks.await(task);
        dataSnapshot=task.getResult();
        Libro libro=dataSnapshot.getValue(Libro.class);
        //libro.setId(dataSnapshot.getKey());
        return libro;
    }

    public List<Libro> getLibros() throws ExecutionException, InterruptedException {
        Task<DataSnapshot> task = this.databaseReference.child("libros").get();
        Tasks.await(task);
        this.dataSnapshot = task.getResult();
      //  List<Libro> lista = dataSnapshot.getValue(new GenericTypeIndicator<List<Libro>>() {
       // });
        List<Libro> libros=new ArrayList<>();
        for(DataSnapshot hijo: dataSnapshot.getChildren()){
            Libro libro= hijo.getValue(Libro.class);
            if(libro!=null){
                libro.setId(hijo.getKey());
                libros.add(libro);
            }
        }
        return libros;
    }
    public List<Prestamo> getPrestamos() throws ExecutionException, InterruptedException {
        Task<DataSnapshot> task=this.databaseReference.child("prestamos").get();
        Tasks.await(task);
        dataSnapshot=task.getResult();
        List<Prestamo> listaPrest=new ArrayList<>();
        for(DataSnapshot child:dataSnapshot.getChildren()){
            Prestamo prestamo=child.getValue(Prestamo.class);
            if(prestamo!=null){
                prestamo.setId(child.getKey());
                listaPrest.add(prestamo);
            }
        }
        return listaPrest;
    }
    public Cliente getCliente(String uid) throws ExecutionException, InterruptedException {
        Task<DataSnapshot> task=databaseReference.child("clientes").child(uid).get();
        Tasks.await(task);
        dataSnapshot= task.getResult();
        Cliente cliente=dataSnapshot.getValue(Cliente.class);
        return cliente;

    }
    public List<Cliente> getClientes() throws ExecutionException, InterruptedException {
        Task<DataSnapshot> task=databaseReference.child("clientes").get();
        Tasks.await(task);
        dataSnapshot=task.getResult();
        List<Cliente> lista=new ArrayList<>();
        for(DataSnapshot child:dataSnapshot.getChildren()){
            Cliente cliente=child.getValue(Cliente.class);
            if(cliente!=null){
                cliente.setId(child.getKey());
                lista.add(cliente);
            }
        }
        return lista;
    }
    public Cliente aniadirCliente(Cliente cliente){
        String id=databaseReference.child("clientes").push().getKey();
        cliente.setId(id);
        this.databaseReference.child("clientes").child(id).setValue(cliente.toMap());
        return cliente;
    }

    public void aniadirPrestamo(Prestamo prestamo){
        String id=this.databaseReference.child("prestamos").push().getKey();
        prestamo.setId(id);
        this.getDatabaseReference().child("prestamos").child(id).setValue(prestamo);
    }

    public void aniadirLibro(Libro libro) {
       String id= this.getDatabaseReference().child("libros").push().getKey();
       libro.setId(id);
       this.getDatabaseReference().child("libros").child(id).setValue(libro.toMap());
    }

    public void modificarLibro(String id, String campo, String valor){
        this.databaseReference.child("libros").child(id).child(campo.toLowerCase()).setValue(valor);

    }

    public void eliminarLibro(String id) throws ExecutionException, InterruptedException {
        List<String> listaPrestamos = new ArrayList<>();
        this.getDatabaseReference().child("libros").child(id).removeValue();
        Task<DataSnapshot> prestamos = this.getDatabaseReference().child("prestamos").get();
        Tasks.await(prestamos);
        this.dataSnapshot = prestamos.getResult();
        ArrayList<String> librosids = new ArrayList<>();
        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
            librosids = (ArrayList<String>) dataSnapshot1.child("librosIds").getValue(); //AQUI VER PORQUE DEVUELVE NULL
            if (librosids != null) {
                for (int i = 0; i < librosids.size(); i++) {
                    if (librosids.get(i).equals(id)) {
                        //listaPrestamos.add(dataSnapshot1.getKey());
                        this.databaseReference.child("prestamos").child(dataSnapshot1.getKey()).child("librosIds").child(""+i).removeValue();
                    }
                }
            }
        }


      /*  for (int i = 0; i < listaPrestamos.size(); i++) {
            this.getDatabaseReference().child("prestamos").child(listaPrestamos.get(i)).child("librosIds").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> librosIdsList = new ArrayList<>();
                    for (DataSnapshot libroSnapshot : snapshot.getChildren()) {
                        librosIdsList.add(libroSnapshot.getValue(String.class));
                        for (int j = 0; j < librosIdsList.size(); j++) {
                            String libroId = librosIdsList.get(j);
                            // Comparar el ID del libro en la base de datos con el ID que queremos eliminar
                            if (libroId.equals(id)) {
                                // Si son iguales, eliminar el libro utilizando removeValue
                                databaseReference.child(String.valueOf(j)).removeValue(); // Usar el índice para eliminar
                                break; // Salir del bucle interno una vez que eliminamos el libro
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
       */
    }
    public void eliminarPrestamo(String id){
        this.getDatabaseReference().child("prestamos").child(id).removeValue();
    }

    public DatabaseReference getDatabaseReference() {
        return this.databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }
}