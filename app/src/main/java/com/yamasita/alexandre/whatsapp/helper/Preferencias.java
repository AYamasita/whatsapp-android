package com.yamasita.alexandre.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.yamasita.alexandre.whatsapp.activity.LoginActivity;

import java.util.HashMap;

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "WHATSAPP_PREFER";
    private SharedPreferences.Editor editor;

    private String CHAVE_NOME = "nome";
    private String CHAVE_TELEFONE = "telefone";
    private String CHAVE_TOKEN = "token";

    /* constructor */
    public Preferencias(Context contextParametro)
    {
        contexto = contextParametro;
        //context.MODE_PRIVATE -> somente o seu app pode utilizar.. não compartilhado = 0
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO,contexto.MODE_PRIVATE);

        editor = preferences.edit();
    }

    public void salvarUsuarioPreferencias(String nome, String telefone, String token)
    {
        try {
            editor.putString(CHAVE_NOME,nome);
            editor.putString(CHAVE_TELEFONE,telefone);
            editor.putString(CHAVE_TOKEN,token);

            editor.commit();

            Log.i("SaveSharedPref","record Shared Preferences");

        }catch(Exception ex)
        {
            Log.e("SaveUserPref: ",ex.toString());
        }

    }

    /**
     * A HashMap however, store items in "key/value" pairs,
     * and you can access them by an index of another type (e.g. a String).
     */
    public HashMap<String,String> getDadosUsuario()
    {

        HashMap<String,String>dadosUsuario = new HashMap <>();

        if(preferences.contains(CHAVE_NOME))
        {
            dadosUsuario.put(CHAVE_NOME, preferences.getString(CHAVE_NOME,null));
        }
        if(preferences.contains(CHAVE_TELEFONE))
        {
            dadosUsuario.put(CHAVE_TELEFONE, preferences.getString(CHAVE_TELEFONE,null));
        }
        if(preferences.contains(CHAVE_TOKEN))
        {
            dadosUsuario.put(CHAVE_TOKEN, preferences.getString(CHAVE_TOKEN,null));
        }

        return dadosUsuario;
    }


}
