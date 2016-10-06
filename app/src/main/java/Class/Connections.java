package Class;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by DIEGO CASALLAS  on 20/03/2015.
 */
public class Connections {
    private int iTipoMensaje;
    private Context contexto;
    public Connections(Context contexto){
        this.iTipoMensaje=0;
        this.contexto=contexto;

    }
    public int redConexion(){

        if(conexionWifi()){

            iTipoMensaje=0;
        }
        else if(conexionRedMovil()){

           iTipoMensaje=1;
        }
        else {

             iTipoMensaje=2;
        }
        return  iTipoMensaje;
    }
    public Boolean conexionWifi(){
        ConnectivityManager connectivity = (ConnectivityManager)contexto.getSystemService(contexto.CONNECTIVITY_SERVICE);
        if(connectivity!=null){
            NetworkInfo info=connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(info!=null){
                if(info.isConnected()){

                    return true;
                }
            }
        }
        return false;
    }
    public Boolean conexionRedMovil(){
        ConnectivityManager connectivity = (ConnectivityManager)contexto.getSystemService(contexto.CONNECTIVITY_SERVICE);
        if(connectivity!=null){
            NetworkInfo info=connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if(info!=null){
                if(info.isConnected()){

                    return true;
                }
            }
        }
        return false;
    }
}

