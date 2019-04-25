package com.yamasita.alexandre.whatsapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.yamasita.alexandre.whatsapp.R;
import com.yamasita.alexandre.whatsapp.helper.Permissao;
import com.yamasita.alexandre.whatsapp.helper.Preferencias;

import java.util.HashMap;
import java.util.Random;

public class LoginActivity extends Activity {

    private EditText editTextNome;

    private EditText editTextTelefone;
    private EditText editTextDDD;
    private EditText editTextNroCountry;

    private Button buttonCadastrar;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //verifica as permissoes
        Permissao.validaPermissoes(1,this, permissoesNecessarias);

        editTextNome =  (EditText) findViewById(R.id.editText);
        editTextTelefone = (EditText) findViewById(R.id.editTelefoneId);
        editTextDDD = (EditText) findViewById(R.id.editTextDDDId);
        editTextNroCountry = (EditText) findViewById(R.id.editTextNroCountryId);

        buttonCadastrar = (Button) findViewById(R.id.buttonCadastrarId);


        /* Define masks*/
        SimpleMaskFormatter msfNroCountry = new SimpleMaskFormatter("+NN");
        MaskTextWatcher mtwNroCountry = new MaskTextWatcher(editTextNroCountry,msfNroCountry);
        editTextNroCountry.addTextChangedListener(mtwNroCountry);

        SimpleMaskFormatter msfDDD = new SimpleMaskFormatter("NN");
        MaskTextWatcher mtwDDD = new MaskTextWatcher(editTextDDD,msfDDD);
        editTextDDD.addTextChangedListener(mtwDDD);

        SimpleMaskFormatter msfTelefone = new SimpleMaskFormatter("NNNNN-NNNN");
        MaskTextWatcher mtwTelefone = new MaskTextWatcher(editTextTelefone,msfTelefone);
        editTextTelefone.addTextChangedListener(mtwTelefone);


        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strNomeUsuario = editTextNome.getText().toString().trim();
                String strNroCountry =  editTextNroCountry.getText().toString().trim();
                String strDDD = editTextDDD.getText().toString().trim();
                String strTelefone = editTextTelefone.getText().toString().trim();

                if(strNomeUsuario.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Por favor, preencha o nome",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(strTelefone.length() == 0)
                    {
                        Toast.makeText(getApplicationContext(),"Por favor, preencha o telefone",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        CompleteCadastro(strNomeUsuario,strNroCountry,strDDD,strTelefone);
                    }
                }
            }
        });
    }

    private void CompleteCadastro(String strNomeUsuario, String strNroCountry,String strDDD, String strTelefone)
    {
        String strTelefoneCompleto = strNroCountry + strDDD + strTelefone;

        String strTelefoneSemFormatacao =  strTelefoneCompleto.replace("+", "");
        strTelefoneSemFormatacao =  strTelefoneSemFormatacao.replace("-", "");
        Log.i("Telefone_Sem_format:", strTelefoneSemFormatacao);

        /* Gerar Token*/
        Random randomico = new Random();
        int numRandomico = randomico.nextInt(9999 - 1000) + 1000;
        String token =  String.valueOf(numRandomico);
        Log.i("Token Gerado:", token);

        //Salvar os dados da Shared Preference
        Preferencias preferencias = new Preferencias(LoginActivity.this);

        preferencias.salvarUsuarioPreferencias(strNomeUsuario,strTelefoneSemFormatacao,token);

        //Envio do SMS
        String strMensagemSMS = "WhatsApp Código de Confirmação: " + token ;

        //usado para teste no emulador.
        strTelefoneSemFormatacao = "5554";

        boolean boolRespEnviandoSMS =   enviaSMS("+" + strTelefoneSemFormatacao,strMensagemSMS );
        Log.i("Envio de SMS", String.valueOf(boolRespEnviandoSMS));

       /*Recuperando os valores no arquivo SharedPreferences*/
        HashMap<String,String> usuario = preferencias.getDadosUsuario();
        Log.i("TOKEN: ", "Nome: " + usuario.get("nome") + " :: T: " + usuario.get("token") );
    }

    private boolean enviaSMS(String telefone, String mensagem)
    {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone,null,mensagem,null,null);
            return true;

        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }




    /*Callback que verifica quais permissoes foram negadas  */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
            for(int resultado: grantResults)
            {
                //se uma permissao foi recusada pelo usuario.
                if (resultado == PackageManager.PERMISSION_DENIED)
                {
                    alertaValidacaoPermissao();
                }
            }
        }
    }

    //Apresenta uma janela de alerta para o usuario devido a negação da permissao.
    private void alertaValidacaoPermissao()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissoes Negadas");

        builder.setMessage("Para utilizar esse App, é necessário aceitar as permisssões");

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
