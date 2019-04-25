package com.yamasita.alexandre.whatsapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    //http://developer.android.com/intl/pt-br/guide/topics/security/permissions.html

    //a permissao é solicitada somente unica vez.
    public static boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes)
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            /**
             * Percorre as permissoes passadas, verificando uma a uma
             * e se já tem a permissão liberada.
             * */

            List<String> listaPermissoes = new ArrayList<>();
            for(String permissao: permissoes)
            {
                //verifica se tem permissao
                Boolean validaPermissao =  ContextCompat.checkSelfPermission(activity,permissao) == PackageManager.PERMISSION_GRANTED;
                if(!validaPermissao)
                {
                    listaPermissoes.add(permissao); //adiciona a permissao
                }
            }
            /* Caso a lista esteja vazia, não é necessario solicitar permissao. */
            if(listaPermissoes.isEmpty())return true;

            //converter a lista de String de permissao em um Array de String
            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //solicita permissao
            ActivityCompat.requestPermissions(activity,novasPermissoes,requestCode);

        }
        return true;
    }
}
