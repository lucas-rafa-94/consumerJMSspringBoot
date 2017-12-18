package com.autoban.errorHandler;

public class LogErroCollection {

    private String idTipoFluxo;
    private String sequencial;
    private String stacktrace;
    private String idOsa;
    private String idConcessionaria;
    private String dsMensagem;
    private String idPassofluxo;
    private String serie;
    private String reprocessado;

    public LogErroCollection() {
        super();
    }

    public LogErroCollection(String idTipoFluxo, String sequencial, String stacktrace, String idOsa,
                             String idConcessionaria, String dsMensagem, String idPassofluxo, String serie, String reprocessado) {
        super();
        this.idTipoFluxo = idTipoFluxo;
        this.sequencial = sequencial;
        this.stacktrace = stacktrace;
        this.idOsa = idOsa;
        this.idConcessionaria = idConcessionaria;
        this.dsMensagem = dsMensagem;
        this.idPassofluxo = idPassofluxo;
        this.serie = serie;
        this.reprocessado = reprocessado;
    }

    public String getIdTipoFluxo() {
        return idTipoFluxo;
    }

    public void setIdTipoFluxo(String idTipoFluxo) {
        this.idTipoFluxo = idTipoFluxo;
    }

    public String getSequencial() {
        return sequencial;
    }

    public void setSequencial(String sequencial) {
        this.sequencial = sequencial;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
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

    public String getDsMensagem() {
        return dsMensagem;
    }

    public void setDsMensagem(String dsMensagem) {
        this.dsMensagem = dsMensagem;
    }

    public String getIdPassofluxo() {
        return idPassofluxo;
    }

    public void setIdPassofluxo(String idPassofluxo) {
        this.idPassofluxo = idPassofluxo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getReprocessado() {
        return reprocessado;
    }

    public void setReprocessado(String reprocessado) {
        this.reprocessado = reprocessado;
    }

}

