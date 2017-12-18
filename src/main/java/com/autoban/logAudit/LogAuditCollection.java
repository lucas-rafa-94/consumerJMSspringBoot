package com.autoban.logAudit;

public class LogAuditCollection {

    private String sequencial;
    private String  idTipoFluxo;
    private String dhmLog;
    private String idOsa;
    private String idConcessionaria;
    private String serie;
    private String idPassofluxo;
    private String dhmPostagemOsa;
    private String dhmRetiradaOsa;
    private String ack;
    private String dsMensagem;


    public LogAuditCollection(String sequencial, String idTipoFluxo, String dhmLog, String idOsa, String idConcessionaria, String serie, String idPassofluxo, String dhmPostagemOsa, String dhmRetiradaOsa, String ack, String dsMensagem) {
        this.sequencial = sequencial;
        this.idTipoFluxo = idTipoFluxo;
        this.dhmLog = dhmLog;
        this.idOsa = idOsa;
        this.idConcessionaria = idConcessionaria;
        this.serie = serie;
        this.idPassofluxo = idPassofluxo;
        this.dhmPostagemOsa = dhmPostagemOsa;
        this.dhmRetiradaOsa = dhmRetiradaOsa;
        this.ack = ack;
        this.dsMensagem = dsMensagem;
    }

    public LogAuditCollection() { }

    public String getSequencial() {
        return sequencial;
    }

    public void setSequencial(String sequencial) {
        this.sequencial = sequencial;
    }

    public String getIdTipoFluxo() {
        return idTipoFluxo;
    }

    public void setIdTipoFluxo(String idTipoFluxo) {
        this.idTipoFluxo = idTipoFluxo;
    }

    public String getDhmLog() {
        return dhmLog;
    }

    public void setDhmLog(String dhmLog) {
        this.dhmLog = dhmLog;
    }

    public String getIdOsa() {
        return idOsa;
    }

    public void setIdOsa(String idOsa) {
        this.idOsa = idOsa;
    }

    public String getIdConcessionaria() {
        return idConcessionaria;
    }

    public void setIdConcessionaria(String idConcessionaria) {
        this.idConcessionaria = idConcessionaria;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getIdPassofluxo() {
        return idPassofluxo;
    }

    public void setIdPassofluxo(String idPassofluxo) {
        this.idPassofluxo = idPassofluxo;
    }

    public String getDhmPostagemOsa() {
        return dhmPostagemOsa;
    }

    public void setDhmPostagemOsa(String dhmPostagemOsa) {
        this.dhmPostagemOsa = dhmPostagemOsa;
    }

    public String getDhmRetiradaOsa() {
        return dhmRetiradaOsa;
    }

    public void setDhmRetiradaOsa(String dhmRetiradaOsa) {
        this.dhmRetiradaOsa = dhmRetiradaOsa;
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public String getDsMensagem() {
        return dsMensagem;
    }

    public void setDsMensagem(String dsMensagem) {
        this.dsMensagem = dsMensagem;
    }
}
