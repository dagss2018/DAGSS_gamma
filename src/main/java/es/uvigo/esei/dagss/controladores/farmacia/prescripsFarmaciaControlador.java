/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.dagss.controladores.farmacia;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.PacienteDAO;
import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;
import es.uvigo.esei.dagss.dominio.entidades.EstadoReceta;
import es.uvigo.esei.dagss.dominio.entidades.Farmacia;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import java.io.Serializable;

import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author noe
 */
@Named(value = "prescripsFarmaciaControlador")
@SessionScoped
public class prescripsFarmaciaControlador implements Serializable {

    private String numTarjeta;
    
    private List<Prescripcion> prescrips;
    
    private Paciente paciente;
    
    private Prescripcion prescripcion;
    
    private List<Receta> recetas;
    
    private EstadoReceta estadoReceta;
    
    private Receta receta;
    
    private Farmacia farmacia;

    @EJB
    PrescripcionDAO prescripcionDAO;
    
    @EJB
    PacienteDAO pacienteDAO;
    
    @EJB 
    RecetaDAO recetaDAO;
    
     @Inject
    private AutenticacionControlador autenticacionControlador;
    
    public String getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    public List<Prescripcion> getPrescrips() {
        return prescrips;
    }

    public void setPrescrips(List<Prescripcion> prescrips) {
        this.prescrips = prescrips;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }
    
    public boolean parametrosInvalidos(){
        return numTarjeta==null;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public void setPrescripcion(Prescripcion prescripcion) {
        this.prescripcion = prescripcion;
    }

    public Prescripcion getPrescripcion() {
        return prescripcion;
    }
    
    public EstadoReceta getEstadoReceta() {
        return estadoReceta;
    }

    public void setEstadoReceta(EstadoReceta estadoReceta) {
        this.estadoReceta = estadoReceta;
    }
    
     public EstadoReceta[] getEstadosRecetas() {
        return EstadoReceta.values();
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }
      
    public String inicializar() {
        String destino = null;
        if (parametrosInvalidos()) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado un numero de tarjeta sanitaria", ""));
        }else{
             this.paciente = pacienteDAO.buscarPorTarjetaSanitaria(this.numTarjeta);
             System.out.println("Paciente"+this.paciente);
             this.prescrips = prescripcionDAO.getPrescripcion(paciente.getId());
             System.out.println("Prescrips"+this.prescrips);
             this.recetas = null;
             destino = "listaPrescrips";
        }
        return destino;
    }
    public String verRecetas(Prescripcion p){
        String destino = null;
        this.prescripcion = p; 
        this.recetas = p.getRecetas();
        if(this.recetas.size() > 0){
            destino = "listaRecetas";
        }else{
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existen recetas para esta prescripción", ""));
        }
        return destino;        
    }
    public void doModificarEstado(Receta rec){
       // System.out.println("Receta: "+rec);
            this.estadoReceta = rec.getEstado();
            this.receta = rec;
    }
    public void doGuardarEstado(){
        this.farmacia  = (Farmacia) this.autenticacionControlador.getUsuarioActual();
        //System.out.println("Farmacia: "+this.farmacia.getNombreFarmacia());
        this.receta.setFarmaciaDispensadora(this.farmacia);
        this.receta.setEstado(this.estadoReceta);
        recetaDAO.actualizar(this.receta);
        recetas = this.prescripcion.getRecetas();
    }
}
