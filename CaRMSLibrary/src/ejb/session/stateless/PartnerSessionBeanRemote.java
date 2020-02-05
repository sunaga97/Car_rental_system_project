/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Partner;
import Entity.Reservation;
import java.util.List;
import util.exception.DeletePartnerException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePartnerException;

/**
 *
 * @author sunag
 */

public interface PartnerSessionBeanRemote {

    public Partner createNewPartner(Partner newPartner) throws InputDataValidationException, UnknownPersistenceException, PartnerUsernameExistException;

    public List<Partner> retrieveAllPartners();

    public Partner retrievePartnerById(Long PartnerId) throws PartnerNotFoundException;

    public void updatePartner(Partner partner) throws InputDataValidationException, PartnerNotFoundException, UpdatePartnerException;

    public void deletePartner(Long partnerId) throws PartnerNotFoundException, DeletePartnerException;

    public Partner login(String username, String password) throws InvalidLoginCredentialException;

    public Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException;

    public List<Reservation> retrieveReservationsFromPartner(Long partnerId);
}
