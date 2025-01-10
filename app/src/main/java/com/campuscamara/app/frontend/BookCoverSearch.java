package com.campuscamara.app.frontend;

import android.os.AsyncTask;

import com.campuscamara.app.model.Libro;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookCoverSearch {
    public static void SearchCover(List<RecyclerAdapter.LibrosIsbn> librosIsbns, Callback callback) {
        new BookCoverTask(librosIsbns, callback).execute();
    }

    public interface Callback {
        void onCoverSearch(List<RecyclerAdapter.LibrosIsbn> librosIsbns);
    }

    private static class BookCoverTask extends AsyncTask<Void, Void, Void> {
        private List<RecyclerAdapter.LibrosIsbn> libros;
        private Callback callback;

        BookCoverTask(List<RecyclerAdapter.LibrosIsbn> lib, Callback callback) {
            this.libros = lib;
            System.out.println(libros.size());
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = 0; i < this.libros.size(); i++) {
                   // synchronized (BookCoverSearch.class) {
                        this.libros.get(i).setUrlcover("https://covers.openlibrary.org/b/isbn/" + getIsbn(i, this.libros.get(i).getTitle()) + "-L.jpg");
                   // }
                }
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
            return null;
        }

        private String getIsbn(int position, String title) throws IOException {
            String url="https://openlibrary.org/search.json?title=";
            String titulo=StringUtils.stripAccents(title.toLowerCase().replace(" ","+").trim());
            url=url+titulo;
            HttpURLConnection conexion = (HttpURLConnection) new URL(url).openConnection();
            conexion.setRequestMethod("GET");
            conexion.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String isbn = parserJSONtoIsbn(response.toString());
            return isbn;
        }
        private String parserJSONtoIsbn(String json){
            String isbn = null;
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray docs = jsonObject.getJSONArray("docs");
                if (docs.length() > 0) {
                    JSONObject firstDoc = docs.getJSONObject(0);
                    if (firstDoc.has("isbn")) {
                        JSONArray isbnArray = firstDoc.getJSONArray("isbn");
                        if (isbnArray.length() > 0) {
                            isbn = isbnArray.getString(0);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return isbn;
        }

        @Override
        protected void onPostExecute(Void voids) {
            this.callback.onCoverSearch(this.libros);
        }
    }
}
