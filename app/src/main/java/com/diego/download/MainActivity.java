package com.diego.download;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import Class.DownloadFile;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private String LOG_TAG = "file";
    private final boolean CANCELAR_SI_MAS_DE_100_IMAGENES = false;
    private final String TAG_LOG = "test";
    private TextView TV_mensaje;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TV_mensaje = (TextView) findViewById(R.id.TextView_mensajesAlUsuario);
        button = (Button) findViewById(R.id.button_probarComoPodemosHacerOtraCosa);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.btnDownload);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.btnDescompress);
        button.setOnClickListener(this);

        videoView=(VideoView)findViewById(R.id.videoView);
        String path=this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        File file = new File(path,"seccion1");
        Log.i("DataView",file.toString());
            if(file.exists()){

            Log.i("DataView","Si");
        }
        else{
            Log.i("DataView","No");
                file.mkdir();

        }
        String zipname = "/Desktop.zip";
        Uri paths=Uri.parse(file+"/amigdalas.mp4");
        videoView.setVideoURI(paths);
        videoView.setMediaController(new MediaController(this));


        videoView.start();
       // Log.i("DataView",spath);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
       /*  String route="http://raulperez.tieneblog.net/wp-content/uploads/2015/09/tux.jpg";
        DownloadFile downloadFile = new DownloadFile(this,this,"","");
        downloadFile.execute(route);
       validateExternalFIle();
        /*File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "TestFile2/imagen.jpg");
        executeFile(file);

       // getAlbumStorageDir(this, "TestFile2");*/

        switch (v.getId()) {

            case R.id.btnDownload:
                String route = "http://www.edibca.com/Android_distribution/Desktop.zip";
                DownloadFile downloadFile = new DownloadFile(this, this, "", "");
                downloadFile.execute(route);
                //getAlbumStorageDir(this, "TestFile2");
                break;
            case R.id.button_probarComoPodemosHacerOtraCosa:
                Log.v(TAG_LOG, "...Haciendo otra cosa el usuario sobre el hilo PRINCIPAL a la vez que carga...");
                Toast toast = Toast.makeText(getApplicationContext(), "...Haciendo otra cosa el usuario sobre el hilo PRINCIPAL a la vez que carga...", Toast.LENGTH_SHORT);
                toast.show();
                DescargarImagenesDeInternetEnOtroHilo miTareaAsincrona = new DescargarImagenesDeInternetEnOtroHilo(CANCELAR_SI_MAS_DE_100_IMAGENES);
                miTareaAsincrona.execute();
                break;
            case R.id.btnDescompress:
                // decompress();
                String spath = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "seccion1").toString();
                String zipname = "/Desktop.zip";
                decompress(spath, zipname);
                break;
        }


    }

    public void decompress() {
        try {
            File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "seccion1");
            ZipInputStream zis = new ZipInputStream(new FileInputStream(file + "/" + "Desktop.zip"));
            ZipEntry entrada;

            while (null != (entrada = zis.getNextEntry())) {

                Toast.makeText(this, entrada.getName(), Toast.LENGTH_LONG).show();
                FileOutputStream fos = new FileOutputStream(entrada.getName());
                int leido;
                byte[] buffers = new byte[1024];
                while (0 < (leido = zis.read(buffers))) {
                    fos.write(buffers, 0, leido);
                }
                fos.close();
                zis.closeEntry();
            }

            Toast.makeText(this, "End", Toast.LENGTH_LONG).show();
        } catch (Exception e) {

            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean decompress(String path, String zipname) {

        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry mZipEntry;
            byte[] buffer = new byte[1024];
            int count;

            while ((mZipEntry = zis.getNextEntry()) != null) {
                // zapis do souboru
                filename = mZipEntry.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (mZipEntry.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path +"/" +filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }



    private class DescargarImagenesDeInternetEnOtroHilo extends AsyncTask<String, Float, Integer> {
        private boolean cancelarSiHayMas100Archivos;
        private ProgressBar miBarraDeProgreso;

        /*
         * Contructor de ejemplo que podemos crear en el AsyncTask
         *
         * @param en este ejemplo le pasamos un booleano que indica si hay más de 100 archivos o no. Si le pasas true se cancela por la mitad del progreso, si le pasas false seguirá hasta el final sin cancelar la descarga simulada
         */
        public DescargarImagenesDeInternetEnOtroHilo(boolean cancelarSiHayMas100Archivos) {
            this.cancelarSiHayMas100Archivos = cancelarSiHayMas100Archivos;
        }

        /*
         * Se ejecuta antes de empezar el hilo en segundo plano. Después de este se ejecuta el método "doInBackground" en Segundo Plano
         *
         * Se ejecuta en el hilo: PRINCIPAL
         */
        @Override
        protected void onPreExecute() {
            TV_mensaje.setText("ANTES de EMPEZAR la descarga. Hilo PRINCIPAL");
            Log.v(TAG_LOG, "ANTES de EMPEZAR la descarga. Hilo PRINCIPAL");

            miBarraDeProgreso = (ProgressBar) findViewById(R.id.progressBar_indicador);
        }

        /*
         * Se ejecuta después de "onPreExecute". Se puede llamar al hilo Principal con el método "publishProgress" que ejecuta el método "onProgressUpdate" en hilo Principal
         *
         * Se ejecuta en el hilo: EN SEGUNDO PLANO
         *
         * @param array con los valores pasados en "execute"
         * @return devuelve un valor al terminar de ejecutar este segundo plano. Se lo envía y ejecuta "onPostExecute" si ha termiado, o a "onCancelled" si se ha cancelado con "cancel"
         */
        @Override
        protected Integer doInBackground(String... variableNoUsada) {

            int cantidadImagenesDescargadas = 0;
            float progreso = 0.0f;

            //Suponemos que tenemos 200 imágenes en algún lado de Internet. isCancelled() comprueba si hemos cancelado con cancel() el hilo en segundo plano.
            while (!isCancelled() && cantidadImagenesDescargadas<200){
                cantidadImagenesDescargadas++;
                Log.v(TAG_LOG, "Imagen descargada número "+cantidadImagenesDescargadas+". Hilo en SEGUNDO PLANO");

                //Simulamos la descarga de una imagen. Iría aquí el código........................
                try {
                    //Simula el tiempo aleatorio de descargar una imagen, al dormir unos milisegundos aleatorios al hilo en segundo plano
                    Thread.sleep((long) (Math.random()*10));
                } catch (InterruptedException e) {
                    cancel(true); //Cancelamos si entramos al catch porque algo ha ido mal
                    e.printStackTrace();
                }
                //Simulamos la descarga de una imagen. Iría aquí el código........................

                progreso+=0.5;

                //Enviamos el progreso a "onProgressUpdate" para que se lo muestre al usuario, pues en el hilo principal no podemos llamar a nada de la interfaz
                publishProgress(progreso);

                //Si hemos decidido cancelar al pasar de 100 imágenes descargadas entramos aquí.
                if (cancelarSiHayMas100Archivos && cantidadImagenesDescargadas>100){
                    cancel(true);
                }
            }

            return cantidadImagenesDescargadas;
        }

        /*
         * Se ejecuta después de que en "doInBackground" ejecute el método "publishProgress".
         *
         * Se ejecuta en el hilo: PRINCIPAL
         *
         * @param array con los valores pasados en "publishProgress"
         */
        @Override
        protected void onProgressUpdate(Float... porcentajeProgreso) {
            TV_mensaje.setText("Progreso descarga: "+porcentajeProgreso[0]+"%. Hilo PRINCIPAL");
            Log.v(TAG_LOG, "Progreso descarga: "+porcentajeProgreso[0]+"%. Hilo PRINCIPAL");

            miBarraDeProgreso.setProgress( Math.round(porcentajeProgreso[0]) );
        }

        /*
         * Se ejecuta después de terminar "doInBackground".
         *
         * Se ejecuta en el hilo: PRINCIPAL
         *
         * @param array con los valores pasados por el return de "doInBackground".
         */
        @Override
        protected void onPostExecute(Integer cantidadProcesados) {
            TV_mensaje.setText("DESPUÉS de TERMINAR la descarga. Se han descarcado "+cantidadProcesados+" imágenes. Hilo PRINCIPAL");
            Log.v(TAG_LOG, "DESPUÉS de TERMINAR la descarga. Se han descarcado "+cantidadProcesados+" imágenes. Hilo PRINCIPAL");

            TV_mensaje.setTextColor(Color.GREEN);
        }

        /*
         * Se ejecuta si se ha llamado al método "cancel" y después de terminar "doInBackground". Por lo que se ejecuta en vez de "onPostExecute"
         * Nota: Este onCancelled solo funciona a partir de Android 3.0 (Api Level 11 en adelante). En versiones anteriores onCancelled no funciona
         *
         * Se ejecuta en el hilo: PRINCIPAL
         *
         * @param array con los valores pasados por el return de "doInBackground".
         */
        @Override
        protected void onCancelled (Integer cantidadProcesados) {
            TV_mensaje.setText("DESPUÉS de CANCELAR la descarga. Se han descarcado "+cantidadProcesados+" imágenes. Hilo PRINCIPAL");
            Log.v(TAG_LOG, "DESPUÉS de CANCELAR la descarga. Se han descarcado "+cantidadProcesados+" imágenes. Hilo PRINCIPAL");

            TV_mensaje.setTextColor(Color.RED);
        }

    }
    public void validateExternalFIle() {

        if (isExternalStorageReadable()) {

            Toast.makeText(this, "yes read and write", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(this, "No read and write", Toast.LENGTH_LONG).show();
        }

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;

    }

    public File getAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    public void executeFile(File file) {


        Uri uri = Uri.fromFile(file);

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "image/*");
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );

            startActivity(intent);
        } catch (Exception e) {


        }
    }
}
