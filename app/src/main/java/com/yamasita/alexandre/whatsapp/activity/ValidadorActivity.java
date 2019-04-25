package com.yamasita.alexandre.whatsapp.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.yamasita.alexandre.whatsapp.R;
import com.yamasita.alexandre.whatsapp.helper.Preferencias;

import java.util.HashMap;

public class ValidadorActivity extends Activity {

    private EditText codigoValidacao;
    private Button btnValidar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        codigoValidacao = (EditText) findViewById(R.id.editTextCodigoSMS);
        btnValidar = (Button)findViewById(R.id.buttonValidar);

        SimpleMaskFormatter simpleMaskCodigoValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher maskCodigoValidacao = new MaskTextWatcher(codigoValidacao,simpleMaskCodigoValidacao);
        codigoValidacao.addTextChangedListener(maskCodigoValidacao);

        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Recuperar dados das preferencias do usuário para validar
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);
                HashMap<String,String> hashMapUsuario =  preferencias.getDadosUsuario();

                String tokenGerado = hashMapUsuario.get("token");
                String tokenDigitado = codigoValidacao.getText().toString();

                if(tokenGerado.equals(tokenDigitado))
                {
                    Toast.makeText(ValidadorActivity.this,"Token Valido.",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ValidadorActivity.this,"Token Invalido",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
