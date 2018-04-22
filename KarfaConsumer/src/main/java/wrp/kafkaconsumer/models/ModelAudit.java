/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrp.kafkaconsumer.models;

/**
 *
 * @author Alonso --- alongo@kriblet.com
 */
public class ModelAudit {

    private AuditType tipo;
    private String destino;
    private String mensaje;

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public AuditType getTipo() {
        return tipo;
    }

    public void setTipo(AuditType tipo) {
        this.tipo = tipo;
    }

    /**
     * enum to identifie the type of message to audit
     */
    public static enum AuditType {
        /**
         * Identifies a message as SMS
         */
        SMS,
        /**
         * Identifies a message as HTML to send it in a mail
         */
        HTML_MAIL,
        /**
         * Identifies a message as plain text to send it in a mail
         */
        MAIL_PLAIN
    }
}
