package com.campuscamara.app.frontend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.campuscamara.app.R;
import com.campuscamara.app.model.BbddHandler;
import com.campuscamara.app.model.Libro;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<Libro> listaOriginal;
    private List<Libro> listaLibro;
    private Context context;
    private BbddHandler bbddHandler;
    private List<LibrosIsbn> listaLibros;
    private LayoutInflater layoutInflater;

    public RecyclerAdapter(List<Libro> listLibro, Context contexto) {
        this.layoutInflater = LayoutInflater.from(contexto);
        this.listaLibro = listLibro;
        this.listaLibros=new ArrayList<>();
        this.context = contexto;
        this.bbddHandler=new BbddHandler(contexto.getResources().getString(R.string.RutaServidor));
        this.listaOriginal = new ArrayList<>();
        this.listaOriginal.addAll(listaLibro);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_element, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Libro libro = this.listaLibro.get(position);
        holder.titulotext.setText("Titulo:");
        holder.autortext.setText("Autor:");
        holder.generotext.setText("Genero:");
        holder.tipotext.setText("Tipo:");
        holder.titulotext.setText(holder.titulotext.getText().toString() + " " + libro.getTitulo());
        holder.autortext.setText(holder.autortext.getText().toString() + " " + libro.getAutor());
        holder.generotext.setText(holder.generotext.getText().toString() + " " + libro.getGenero());
        holder.tipotext.setText(holder.tipotext.getText().toString() + " " + libro.getTipo());
        Glide.with(context).load(this.listaLibro.get(position).getCoverurl()).placeholder(R.drawable.placeholder).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),LibroDetallesAct.class);
                intent.putExtra("libroid",libro.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void updateCover() {
        boolean bol=false;
        List<Integer>bookwithoutcover=new ArrayList<>();
        int i=0;
        int cont=0;
        while(i<this.listaOriginal.size() && this.listaOriginal.get(i).getCoverurl()!=null){
            i++;
        }
        if(i!=listaOriginal.size() || listaOriginal.get(i-1).getCoverurl()==null){
            bol=true;
        }
        /*
        if(bol) {
            for (int j = 0; j < listaOriginal.size(); j++) {
                this.listaLibros.add(new LibrosIsbn(listaOriginal.get(j).getId(), listaOriginal.get(j).getTitulo()));
            }
            BookCoverSearch.SearchCover(this.listaLibros, new BookCoverSearch.Callback() {
                @Override
                public void onCoverSearch(List<LibrosIsbn> librosIsbns) {
                    for (int i = 0; i < listaLibros.size(); i++) {
                        listaOriginal.get(i).setCoverurl(listaLibros.get(i).urlcover);
                        bbddHandler.modificarLibro(listaOriginal.get(i).getId(), "coverurl", listaOriginal.get(i).getCoverurl());
                    }
                    listaLibro.clear();
                    listaLibro.addAll(listaOriginal);
                    notifyDataSetChanged();
                }
            });
         */
        if(bol) {
            for (int j = 0; j < listaOriginal.size(); j++) {
                if (listaOriginal.get(j).getCoverurl() == null) {
                    this.listaLibros.add(
                            new LibrosIsbn(listaOriginal.get(j).getId(), listaOriginal.get(j).getTitulo())
                    );
                }
            }
            BookCoverSearch.SearchCover(this.listaLibros, new BookCoverSearch.Callback() {
                @Override
                public void onCoverSearch(List<LibrosIsbn> librosIsbns) {
                    int index = 0;
                    for (int j = 0; j < listaOriginal.size(); j++) {
                        if (listaOriginal.get(j).getCoverurl() == null) {
                            listaOriginal.get(j).setCoverurl(librosIsbns.get(index).urlcover);
                            bbddHandler.modificarLibro(
                                    listaOriginal.get(j).getId(),
                                    "coverurl",
                                    listaOriginal.get(j).getCoverurl()
                            );
                            index++;
                        }
                    }
                    listaLibro.clear();
                    listaLibro.addAll(listaOriginal);
                    notifyDataSetChanged();
                }
            });
        }
    }
    public void filtrarTitulo(String texto) {
        int longitud = texto.length();
        listaLibro.clear();
        if (longitud == 0) {
            listaLibro.addAll(this.listaOriginal);
            //this.listaLibrosFI.addAll(listaLibros);
        } else {
            for (Libro libro : listaOriginal) {
                if(libro.getTitulo()!=null && texto!=null) {
                    if (StringUtils.stripAccents(libro.getTitulo().toLowerCase()).contains(texto.toLowerCase())) {
                        listaLibro.add(libro);
                    }
                }
            }
            }
            notifyDataSetChanged();
        }
    public void filtrarAutor(String texto) {
        int longitud = texto.length();
        listaLibro.clear();
        if (longitud == 0) {
            listaLibro.addAll(this.listaOriginal);
        } else {
            for (Libro libro : listaOriginal) {
                if (libro.getAutor() != null && texto != null) {
                    if (StringUtils.stripAccents(libro.getAutor().toLowerCase()).contains(texto.toLowerCase())) {
                        listaLibro.add(libro);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filtrarGenero(String texto) {
        int longitud = texto.length();
        listaLibro.clear();
        if (longitud == 0) {
            listaLibro.addAll(this.listaOriginal);
        } else {
            for (Libro libro : listaOriginal) {
                if (libro.getGenero() != null && texto != null) {
                    if (StringUtils.stripAccents(libro.getGenero().toLowerCase()).contains(texto.toLowerCase())) {
                        listaLibro.add(libro);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return listaLibro.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titulotext;
        private TextView autortext;
        private TextView generotext;
        private TextView tipotext;
        public ImageView image;

        public ViewHolder(@NonNull View view) {
            super(view);
            titulotext = view.findViewById(R.id.titulotv);
            autortext = view.findViewById(R.id.autortv);
            generotext = view.findViewById(R.id.generotv);
            tipotext = view.findViewById(R.id.tipotv);
            image = view.findViewById(R.id.imageView);
        }

    }

    public class LibrosIsbn {
        private String id;
        private String title;
        private String urlcover;

        public LibrosIsbn(String id,String t) {
            this.setId(id);
            this.setTitle(t);
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public void setUrlcover(String urlcover) {
            this.urlcover = urlcover;
        }

        public void setId(String id){
            this.id=id;
        }
        public String getId(){
            return this.id;
        }
    }
}
