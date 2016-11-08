package Class;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by DIEGO  CASALLAS  on 15/06/2016.
 */
public class DownloadFile extends AsyncTask<String, String, String> {
    public  final static String URI_WEB="/storage/emulated/0/Android/data/com.diego.download/file/Pictures";



    private ProgressDialog pDialog;
    private Activity activity;
    private Context context;
    private String sDirPath;
    private String sNameArchive;
    private long s;
    private File fDir;
    public boolean bValidateDownload;
    public boolean bValidateCancel;
    public Connections ObjConexion;

    public DownloadFile(Activity activity, Context context, String sDirPath, String sNameArchive) {
        this.activity = activity;
        this.context = context;
        this.sDirPath = sDirPath;
        this.sNameArchive = sNameArchive;
        this.bValidateDownload = false;
        this.bValidateCancel = true;
        this.fDir = null;
        this.pDialog = new ProgressDialog(activity);


    }


    @Override
    protected void onPostExecute(String s) {

        // super.onPreExecute();
        pDialog.dismiss();
        // String imagePath = dataDirPath+"/"+nameArchive;
        // setting downloaded into image view
        //my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        //dir= new File(imagePath);
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        if(bValidateDownload && bValidateCancel){
            // Setting Dialog Title
            alertDialog.setTitle("Síndrome Coronario ");
            // Setting Dialog Message
            alertDialog.setMessage("Descarga completa");
            // Setting OK Button
            alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, final int which) {

                    //clase_control= new Class_control(mainActivity);

                    //clase_control.executeFile(dataDirPath+"/"+nameArchive,bType);
                }
            });
        }
        else{
            // Setting Dialog Title
            alertDialog.setTitle("Síndrome Coronario ");
            // Setting Dialog Message
            alertDialog.setMessage("Error al  realizar la  descarga");
            // Setting OK Button
            alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog,final int which) {


                }
            });
            //eliminarContenido(imagePath);

        }
        bValidateCancel=true;
        bValidateDownload=false;


        // Showing Alert Message
        alertDialog.show();

    }

    @Override
    protected String doInBackground(String... params) {
        int iConut=0;
        try
        {

            URL url=new URL(params[0]);
            URLConnection connection=url.openConnection();
            connection.connect();
            int lenghtOfFile=connection.getContentLength();

            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "seccion1");

            InputStream inputStream=new BufferedInputStream(url.openStream(),8192);
            OutputStream outputStream=new FileOutputStream(file+"/"+"Desktop.zip");
            byte data[]=new byte[1024];
            long total=0;
            while (((iConut = inputStream.read(data)) != -1)&& bValidateCancel) {
                if(ValidaEstadoConexion()) {
                    total += iConut;
                }
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                // writing data to file
                s=(int) ((total * 100) / lenghtOfFile);


                outputStream.write(data, 0, iConut);
            }
            if(total==lenghtOfFile){

                bValidateDownload=true;
            }
            else{

                pDialog.setProgress(0);
                publishProgress(""+0);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {

            Log.e("Error: ", e.getMessage());
        }


        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        this.pDialog.setProgress(Integer.parseInt(values[0]));
    }

   /* @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog.setMessage("Descargando  contenido. Favor espere...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pDialog.setProgress(0);
                bValidateCancel = false;
                dialog.dismiss();
            }
        });
        pDialog.show();
    }

    public  boolean ValidaEstadoConexion(){

        ObjConexion=new Connections(activity);
        if(ObjConexion.redConexion()==1 || ObjConexion.redConexion()==0){


            return true;

        }
        return false;
    }


}
