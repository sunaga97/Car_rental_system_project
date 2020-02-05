/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmscustomerclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.TransitRecordSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Goh Chun Teck
 */
public class Main {

    @EJB
    private static TransitRecordSessionBeanRemote transitRecordSessionBean;

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBean;

    @EJB
    private static CarSessionBeanRemote carSessionBean;

    @EJB
    private static OutletSessionBeanRemote outletSessionBean;

    @EJB
    private static CarModelSessionBeanRemote carModelSessionBean;

    @EJB
    private static CarCategorySessionBeanRemote carCategorySessionBean;

    @EJB
    private static PartnerSessionBeanRemote partnerSessionBean;

    @EJB
    private static CustomerSessionBeanRemote customerSessionBean;

    
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(customerSessionBean, carCategorySessionBean, carModelSessionBean, outletSessionBean, carSessionBean, reservationSessionBean, transitRecordSessionBean);
        mainApp.runApp();
    }
    
}
