package com.app.helper;


import com.app.bean.JmsConnectionFactory;
import com.app.bean.JmsServerConnection;
import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class GetProperties {

    private static final Log log = LogFactory.getFactory().getInstance(GetProperties.class);

    public static HashMap<String, JmsConnectionFactory> getProperties(){

        HashMap<String, JmsConnectionFactory> jmsProperites = new HashMap<>();

        try {

            FileReader fileReader = new FileReader("/u01/oracle/domains/ccr_osbgtw_domain/config/app/o2MinJndiConnection.properties");
            BufferedReader br = new BufferedReader(fileReader);
            String linha = br.readLine();
            String resultado = "";

            Gson gson = new Gson();

            while (linha != null) {
                jmsProperites.put(linha.split("=")[0].trim(),
                        gson.fromJson(linha.split("=")[1].trim(), JmsConnectionFactory.class));
                resultado += linha;
                linha = br.readLine();
            }

        } catch (Exception e) {
            log.error("Erro ao ler properties  o2MinConnectionServerJms " + e.getMessage());

        }
        return jmsProperites;
    }


    public static HashMap<String, JmsServerConnection> getServerProperties() {

        HashMap<String, JmsServerConnection> jmsServerProperites = new HashMap<>();

        try {

            FileReader fileReader = new FileReader("/u01/oracle/domains/ccr_osbgtw_domain/config/app/o2MinConnectionServerJms.properties");
            BufferedReader br = new BufferedReader(fileReader);
            String linha = br.readLine();
            String resultado = "";

            Gson gson = new Gson();

            while (linha != null) {
                jmsServerProperites.put(linha.split("=")[0].trim(),
                        gson.fromJson(linha.split("=")[1].trim(), JmsServerConnection.class));
                resultado += linha;
                linha = br.readLine();
            }



        } catch (Exception e) {
            log.error("Erro ao ler properties  o2MinConnectionServerJms " + e.getMessage());

        }

        return jmsServerProperites;
    }
}
