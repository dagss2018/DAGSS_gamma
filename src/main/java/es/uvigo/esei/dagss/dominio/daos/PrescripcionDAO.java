/*
 Proyecto Java EE, DAGSS-2016
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class PrescripcionDAO extends GenericoDAO<Prescripcion> {

    public Prescripcion buscarPorIdConRecetas(Long id) {
        TypedQuery<Prescripcion> q = em.createQuery("SELECT p FROM Prescripcion AS p JOIN FETCH p.recetas  WHERE p.recetas.id = :id", Prescripcion.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }
    public List<Prescripcion> getPrescripcion(Long id){
        TypedQuery<Prescripcion> q = em.createQuery("SELECT p FROM Prescripcion AS p WHERE p.paciente.id = :paciente", Prescripcion.class);
        q.setParameter("paciente", id);
        return q.getResultList();
    }
    public List<Prescripcion> buscarPorPaciente(Long id, String date) {
        TypedQuery<Prescripcion> q = em.createQuery("SELECT p FROM Prescripcion AS p JOIN FETCH p.paciente  WHERE p.paciente.id = :id AND (p.fechaFin) > :date ", Prescripcion.class);
        q.setParameter("id", id);
        q.setParameter("date", date);

        return q.getResultList();
    }
}
