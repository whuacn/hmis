/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.ws.channel;

import com.divudi.bean.common.BillBeanController;
import com.divudi.bean.common.CommonController;
import com.divudi.data.BillClassType;
import com.divudi.data.BillType;
import com.divudi.data.FeeType;
import com.divudi.data.HistoryType;
import com.divudi.data.PaymentMethod;
import com.divudi.data.PersonInstitutionType;
import com.divudi.ejb.BillNumberGenerator;
import com.divudi.ejb.ChannelBean;
import com.divudi.ejb.CommonFunctions;
import com.divudi.ejb.ServiceSessionBean;
import com.divudi.entity.AgentHistory;
import com.divudi.entity.Bill;
import com.divudi.entity.BillFee;
import com.divudi.entity.BillItem;
import com.divudi.entity.BillSession;
import com.divudi.entity.BilledBill;
import com.divudi.entity.CancelledBill;
import com.divudi.entity.Institution;
import com.divudi.entity.Item;
import com.divudi.entity.ItemFee;
import com.divudi.entity.Patient;
import com.divudi.entity.Person;
import com.divudi.entity.RefundBill;
import com.divudi.entity.ServiceSession;
import com.divudi.facade.AgentHistoryFacade;
import com.divudi.facade.BillFacade;
import com.divudi.facade.BillFeeFacade;
import com.divudi.facade.BillItemFacade;
import com.divudi.facade.BillSessionFacade;
import com.divudi.facade.InstitutionFacade;
import com.divudi.facade.ItemFeeFacade;
import com.divudi.facade.PatientFacade;
import com.divudi.facade.PersonFacade;
import com.divudi.facade.ServiceSessionFacade;
import com.divudi.facade.StaffFacade;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.TemporalType;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Archmage-Dushan
 */
@Path("api")
@RequestScoped
public class Api {

    @Context
    private UriInfo context;

    @EJB
    StaffFacade staffFacade;
    @EJB
    private ItemFeeFacade ItemFeeFacade;
    @EJB
    private BillSessionFacade billSessionFacade;
    @EJB
    private ServiceSessionFacade serviceSessionFacade;
    @EJB
    private InstitutionFacade institutionFacade;
    @EJB
    private BillFacade billFacade;
    @EJB
    private BillItemFacade billItemFacade;
    @EJB
    private BillFeeFacade billFeeFacade;
    @EJB
    private AgentHistoryFacade agentHistoryFacade;
    @EJB
    private PatientFacade patientFacade;
    @EJB
    private PersonFacade personFacade;

    @EJB
    private CommonFunctions commonFunctions;
    @EJB
    private ChannelBean channelBean;
    @EJB
    private BillNumberGenerator billNumberBean;
    @EJB
    private ServiceSessionBean serviceSessionBean;

    @Inject
    private BillBeanController billBeanController;
    @Inject
    private CommonController commonController;

    /**
     * Creates a new instance of Api
     */
    public Api() {
    }

    /**
     * Retrieves representation of an instance of com.divudi.ws.channel.Api
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/json")
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        return "<html><body><h1>Hello, World!!</body></h1></html>";
    }

    @GET
    @Path("/json2")
    @Produces("text/html")
    public String getJson2() {
        //TODO return proper representation object
        return "<html><body><h1>Hello, World 2!!</body></h1></html>";
    }

    @GET
    @Path("/doctors")
    @Produces("application/json")
    public String getDoctors() {

        List<Object[]> consultants = doctorsList(null);
        JSONArray array = new JSONArray();
        JSONObject jSONObjectOut = new JSONObject();
        if (!consultants.isEmpty()) {
            for (Object[] con : consultants) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("doc_id", con[0]);
                jSONObject.put("doc_name", con[1]);
                jSONObject.put("doc_specility", con[2]);
                jSONObject.put("doc_code", con[3]);
                array.put(jSONObject);
            }
            jSONObjectOut.put("doctors", array);
            jSONObjectOut.put("error", "0");
            jSONObjectOut.put("error_description", "");
        } else {
            jSONObjectOut.put("doctors", array);
            jSONObjectOut.put("error", "1");
            jSONObjectOut.put("error_description", "No Data.");
        }

        String json = jSONObjectOut.toString();

        return json;
    }

    @GET
    @Path("/doctors/{doc_code}")
    @Produces("application/json")
    public String getDoctor(@PathParam("doc_code") String doc_code) {

        JSONArray array = new JSONArray();
        JSONObject jSONObjectOut = new JSONObject();
        try {
//            Long d_id = Long.parseLong(doc_id);
            List<Object[]> consultants = doctorsList(doc_code);
            if (!consultants.isEmpty()) {
                for (Object[] con : consultants) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("doc_id", con[0]);
                    jSONObject.put("doc_name", con[1].toString().toUpperCase());
                    jSONObject.put("doc_specility", con[2].toString().toUpperCase());
                    jSONObject.put("doc_code", con[3]);
                    array.put(jSONObject);
                }
                jSONObjectOut.put("doctors", array);
                jSONObjectOut.put("error", "0");
                jSONObjectOut.put("error_description", "");
            } else {
                jSONObjectOut.put("doctors", array);
                jSONObjectOut.put("error", "1");
                jSONObjectOut.put("error_description", "No Data.");
            }

        } catch (Exception e) {
            jSONObjectOut.put("doctors", array);
            jSONObjectOut.put("error", "1");
            jSONObjectOut.put("error_description", "Invalid Argument.");
        }
        String json = jSONObjectOut.toString();

        return json;
    }

//    @GET
//    @Path("/users/{userid}")
//    @Produces(MediaType.APPLICATION_XML)
//    public User getUser(@PathParam("userid") int userid) {
//        return userDao.getUser(userid);
//    }
    @GET
    @Path("/sessions/{doc_code}")
    @Produces("application/json")
    public String getSessions(@PathParam("doc_code") String doc_code) {

        JSONObject object = new JSONObject();
        JSONObject jSONObjectOut = new JSONObject();

        try {
            List<Object[]> sessions = sessionsList(doc_code, null, null);
            if (!sessions.isEmpty()) {
                for (Object[] s : sessions) {
                    object.put("session_id", s[0]);
                    object.put("session_date", getCommonController().getDateFormat((Date) s[1]));
                    object.put("session_starting_time", getCommonController().getTimeFormat24((Date) s[2]));
                    object.put("session_ending_time", getCommonController().getTimeFormat24((Date) s[3]));
                    object.put("session_max_no", s[4]);
                    object.put("session_is_refundable", s[5]);
                    object.put("session_duration", s[6]);
                    object.put("session_room_no", s[7]);
                    object.put("session_current_app_no", channelBean.getBillSessionsCount((long) s[0], (Date) s[1]));
                    object.put("session_fee", getCommonController().getDouble((double) fetchLocalFee((long) s[0], PaymentMethod.Agent, false)));
                    object.put("session_is_leaved", s[10]);
                    System.out.println("s.length = " + s.length);
//            s[10]=fetchLocalFee((long)s[0], PaymentMethod.Agent, true);
                }
                jSONObjectOut.put("session", sessions);
                jSONObjectOut.put("error", "0");
                jSONObjectOut.put("error_description", "");
            } else {
                jSONObjectOut.put("session", sessions);
                jSONObjectOut.put("error", "1");
                jSONObjectOut.put("error_description", "No Data.");
            }
        } catch (Exception e) {
            jSONObjectOut.put("session", object);
            jSONObjectOut.put("error", "1");
            jSONObjectOut.put("error_description", "Invalid Argument.");
        }

        String json = jSONObjectOut.toString();
//        String json = new Gson().toJson(sessions);

        return json;
    }

    @GET
    @Path("/makeBooking/{name}/{title}/{phone}/{hospital_id}/{session_id}/{doc_code}/{agent_id}/{agent_reference_no}")
    @Produces("application/json")
    public String makeBooking(@PathParam("name") String name, @PathParam("title") String title, @PathParam("phone") String phone,
            @PathParam("hospital_id") String hospital_id, @PathParam("session_id") String session_id, @PathParam("doc_code") String doc_code,
            @PathParam("agent_id") String agent_id, @PathParam("agent_reference_no") String agent_reference_no) {

        JSONArray bill = new JSONArray();
        String json = new String();
        List<Object[]> list = new ArrayList<>();
        JSONObject jSONObjectOut = new JSONObject();
        Long h_id = Long.parseLong(hospital_id);
        Long ss_id = Long.parseLong(session_id);
        Long a_id = Long.parseLong(agent_id);
        Long ar_no = Long.parseLong(agent_reference_no);
        try {

            String s = fetchErrors(name, phone, doc_code, ss_id, a_id, ar_no);
            System.out.println("s = " + s);
            if (!"".equals(s)) {
                jSONObjectOut.put("make_booking", s);
                jSONObjectOut.put("error", "1");
                jSONObjectOut.put("error_description", "No Data.");
            }
            ServiceSession ss = serviceSessionFacade.find(ss_id);
            if (ss != null) {
                //For Settle bill
                ss.setTotalFee(fetchLocalFee(ss.getOriginatingSession(), PaymentMethod.Agent));
                ss.setTotalFfee(fetchForiegnFee(ss.getOriginatingSession(), PaymentMethod.Agent));
                ss.setItemFees(fetchFee(ss.getOriginatingSession()));
                //For Settle bill
                ss.getOriginatingSession().setTotalFee(fetchLocalFee(ss.getOriginatingSession(), PaymentMethod.Agent));
                ss.getOriginatingSession().setTotalFfee(fetchForiegnFee(ss.getOriginatingSession(), PaymentMethod.Agent));
                ss.getOriginatingSession().setItemFees(fetchFee(ss.getOriginatingSession()));
            } else {
                jSONObjectOut.put("make_booking", s);
                jSONObjectOut.put("error", "1");
                jSONObjectOut.put("error_description", "No Data.");
                return jSONObjectOut.toString();
            }
            System.out.println("ss = " + ss);

            Bill b = saveBilledBill(ss, name, phone, doc_code, a_id, ar_no);
            System.out.println("b = " + b);

            bill = billDetails(b.getId());
            jSONObjectOut.put("make_booking", bill);
            jSONObjectOut.put("error", "0");
            jSONObjectOut.put("error_description", "");
        } catch (Exception e) {
            jSONObjectOut.put("make_booking", bill);
            jSONObjectOut.put("error", "1");
            jSONObjectOut.put("error_description", "Invalid Argument.");
        }

        json = jSONObjectOut.toString();

        return json;
    }

//    @POST
//    @Path("/makeBooking2/")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void makeBooking2(JSONObject inputJsonObj) {
//        String input = (String) inputJsonObj.get("input");
//        String output = "The input you sent is :" + input;
//        JSONObject outputJsonObj = new JSONObject();
//        outputJsonObj.put("output", output);
//    }
    @GET
    @Path("/bookings/{bill_id}")
    @Produces("application/json")
    public String getBookings(@PathParam("bill_id") String bill_id) {
//        /bookings/20058204
        JSONObject jSONObjectOut = new JSONObject();
        JSONArray bill = new JSONArray();
        try {
            long b_id = Long.parseLong(bill_id);
            bill = billDetails(b_id);
            if (bill != null) {
                jSONObjectOut.put("bookings", bill);
                jSONObjectOut.put("error", "0");
                jSONObjectOut.put("error_description", "");
            } else {
                jSONObjectOut.put("bookings", bill);
                jSONObjectOut.put("error", "1");
                jSONObjectOut.put("error_description", "No Data.");
            }
        } catch (Exception e) {
            jSONObjectOut.put("bookings", bill);
            jSONObjectOut.put("error", "1");
            jSONObjectOut.put("error_description", "Invalid Argument.");
        }
//        String json = new Gson().toJson(bill);
        String json = jSONObjectOut.toString();
        return json;
    }

    @GET
    @Path("/bookings/{agent_id}/{from_date}/{to_date}")
    @Produces("application/json")
    public String getAllBookings(@PathParam("agent_id") String agent_id, @PathParam("from_date") String from_date, @PathParam("to_date") String to_date) {
//        /bookings/20058554/2016-08-01/2016-08-15
        JSONObject jSONObjectOut = new JSONObject();
        JSONArray bill = new JSONArray();
        try {
            long b_id = Long.parseLong(agent_id);
            Date fromDate = getCommonController().getConvertDateTimeFormat24(from_date);
            Date toDate = getCommonController().getConvertDateTimeFormat24(to_date);
            bill = billsDetails(b_id, fromDate, toDate, true);
            if (bill != null) {
                jSONObjectOut.put("bookings", bill);
                jSONObjectOut.put("error", "0");
                jSONObjectOut.put("error_description", "");
            } else {
                jSONObjectOut.put("bookings", bill);
                jSONObjectOut.put("error", "1");
                jSONObjectOut.put("error_description", "No Data.");
            }
        } catch (Exception e) {
            jSONObjectOut.put("bookings", bill);
            jSONObjectOut.put("error", "1");
            jSONObjectOut.put("error_description", "Invalid Argument.");
        }
//        String json = new Gson().toJson(bill);
        String json = jSONObjectOut.toString();
        return json;
    }

    //----------------------------------------------------
    public List<Object[]> doctorsList(String doc_code) {

        List<Object[]> consultants = new ArrayList<>();
        String sql;
        Map m = new HashMap();

        sql = " select pi.staff.id,"
                + " pi.staff.person.name, "
                + " pi.staff.speciality.name,"
                + " pi.staff.code from PersonInstitution pi where pi.retired=false "
                + " and pi.type=:typ "
                + " and pi.staff.retired=false "
                + " and pi.institution is not null "
                + " and pi.staff.speciality is not null ";

        if (doc_code != null) {
            sql += " and pi.staff.code=:doc_code ";
            m.put("doc_code", doc_code);
        }
//        if (doc_id != null) {
//            sql += " and pi.staff.id=:doc_id ";
//            m.put("doc_id", doc_id);
//        }

        sql += " order by pi.staff.person.name ";

        m.put("typ", PersonInstitutionType.Channelling);
        consultants = getStaffFacade().findAggregates(sql, m);

        System.out.println("m = " + m);
        System.out.println("sql = " + sql);
        System.out.println("consultants.size() = " + consultants.size());

        return consultants;
    }

    public List<Object[]> sessionsList(String doc_code, Date fromDate, Date toDate) {

        List<Object[]> sessions = new ArrayList<>();
        String sql;
        Map m = new HashMap();

        sql = "Select s.id, "
                + " s.sessionDate, "
                + " s.startingTime, "
                + " s.endingTime, "
                + " s.maxNo, "
                + " s.refundable, "
                + " s.duration, "
                + " s.roomNo, "
                + " 0, "
                + " 0,"
                + " s.deactivated "
                + " From ServiceSession s where s.retired=false "
                + " and s.staff.code=:doc_code "
                + " and s.originatingSession is not null "
                + " and type(s)=:class ";
        if (fromDate != null && toDate != null) {
            sql += " and s.sessionDate between :fd and :td ";
            m.put("fd", fromDate);
            m.put("td", toDate);
        } else {
            sql += " and s.sessionDate >= :nd ";
            m.put("nd", commonFunctions.getStartOfDay());
        }

        sql += " order by s.sessionDate,s.startingTime ";

        m.put("doc_code", doc_code);
        m.put("class", ServiceSession.class);

        sessions = getStaffFacade().findAggregates(sql, m);

        System.out.println("m = " + m);
        System.out.println("sql = " + sql);
        System.out.println("sessions.size() = " + sessions.size());

        return sessions;
    }

    public JSONArray billDetails(long billId) {
        List<BillSession> billObjects;
        JSONArray array = new JSONArray();
        String sql;
        Map m = new HashMap();

        sql = "Select bs From BillSession bs "
                + " where bs.bill.id=:id ";

        m.put("id", billId);
        billObjects = billSessionFacade.findBySQL(sql, m);

        System.out.println("m = " + m);
        System.out.println("sql = " + sql);
        System.out.println("billObjects.length = " + billObjects.size());

        Map map = new HashMap();
        if (!billObjects.isEmpty()) {

            try {
                map.put("bill_id", billObjects.get(0).getBill().getId());
                map.put("bill_number", billObjects.get(0).getBill().getInsId());
                map.put("bill_agent", billObjects.get(0).getBill().getCreditCompany().getId());
                map.put("bill_app_no", billObjects.get(0).getBill().getSingleBillSession().getSerialNo());
                map.put("bill_patient_name", billObjects.get(0).getBill().getPatient().getPerson().getName());
                map.put("bill_phone", billObjects.get(0).getBill().getPatient().getPerson().getPhone());
                map.put("bill_doc_name", billObjects.get(0).getBill().getStaff().getPerson().getName());
                map.put("bill_session_date", getCommonController().getDateFormat(billObjects.get(0).getBill().getSingleBillSession().getSessionDate()));
                map.put("bill_session_start_time", getCommonController().getTimeFormat24(billObjects.get(0).getBill().getSingleBillSession().getServiceSession().getStartingTime()));
                map.put("bill_created_at", getCommonController().getDateTimeFormat24(billObjects.get(0).getBill().getCreatedAt()));
                map.put("bill_total", getCommonController().getDouble(billObjects.get(0).getBill().getNetTotal() + billObjects.get(0).getBill().getVat()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("map.length = " + map.size());
        array.put(map);

        return array;
    }

    public JSONArray billsDetails(long agentId, Date fromDate, Date toDate, boolean createDate) {
        List<BillSession> billObjects;
        JSONArray array = new JSONArray();
        String sql;
        Map m = new HashMap();

        sql = "Select bs From BillSession bs "
                + " where bs.bill.creditCompany.id=:id ";
        if (createDate) {
            sql += " and bs.bill.createdAt between :fd and :td "
                    + " order by bs.bill.createdAt ";
        } else {
            sql += " and bs.bill.singleBillSession.sessionDate between :fd and :td "
                    + " order by bs.bill.singleBillSession.sessionDate ";
        }

        m.put("id", agentId);
        m.put("fd", commonFunctions.getStartOfDay(fromDate));
        m.put("td", commonFunctions.getEndOfDay(toDate));
        billObjects = billSessionFacade.findBySQL(sql, m, TemporalType.TIMESTAMP);

        System.out.println("m = " + m);
        System.out.println("sql = " + sql);
        System.out.println("billObjects.length = " + billObjects.size());

        for (BillSession o : billObjects) {
            try {
                JSONObject map = new JSONObject();
                map.put("bill_id", o.getBill().getId());
                map.put("bill_number", o.getBill().getInsId());
                map.put("bill_agent", o.getBill().getCreditCompany().getId());
                map.put("bill_app_no", o.getBill().getSingleBillSession().getSerialNo());
                map.put("bill_patient_name", o.getBill().getPatient().getPerson().getName());
                map.put("bill_phone", o.getBill().getPatient().getPerson().getPhone());
                map.put("bill_doc_name", o.getBill().getStaff().getPerson().getName());
                map.put("bill_session_date", getCommonController().getDateFormat(o.getBill().getSingleBillSession().getSessionDate()));
                map.put("bill_session_start_time", getCommonController().getTimeFormat24(o.getBill().getSingleBillSession().getServiceSession().getStartingTime()));
                map.put("bill_created_at", getCommonController().getDateTimeFormat24(o.getBill().getCreatedAt()));
                map.put("bill_total", getCommonController().getDouble(o.getBill().getNetTotal() + o.getBill().getVat()));
                array.put(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return array;
    }

    //----------------------------------------------------
    double fetchLocalFee(long id, PaymentMethod paymentMethod, boolean forign) {
        String jpql;
        Map m = new HashMap();
        FeeType[] fts = {FeeType.Service, FeeType.OwnInstitution, FeeType.Staff};
        List<FeeType> feeTypes = Arrays.asList(fts);
        if (forign) {
            jpql = "Select sum(f.ffee)";
        } else {
            jpql = "Select sum(f.fee)";
        }

        jpql += " from ItemFee f "
                + " where f.retired=false "
                + " and f.item.id=:ses ";

        if (paymentMethod == PaymentMethod.Agent) {
            FeeType[] fts1 = {FeeType.Service, FeeType.OwnInstitution, FeeType.Staff, FeeType.OtherInstitution};
            feeTypes = Arrays.asList(fts1);
            jpql += " and f.feeType in :fts1 "
                    + " and f.name!=:name";
            m.put("name", "On-Call Fee");
            m.put("fts1", feeTypes);
        } else {
            if (paymentMethod == PaymentMethod.OnCall) {
                jpql += " and f.feeType in :fts2 ";
                m.put("fts2", feeTypes);
            } else {
                jpql += " and f.feeType in :fts3 "
                        + " and f.name!=:name";
                m.put("name", "On-Call Fee");
                m.put("fts3", feeTypes);
            }
        }
        m.put("ses", serviceSessionFacade.find(id).getOriginatingSession().getId());
//        System.out.println("paymentMethod = " + paymentMethod);
//        System.out.println("feeTypes = " + feeTypes);
//        System.out.println("m = " + m);
        Double obj = ItemFeeFacade.findDoubleByJpql(jpql, m);
        System.out.println("obj = " + obj);
        if (obj == null) {
            return 0;
        }

        return obj;
    }

    String fetchErrors(String name, String phone, String doc, long ses, long agent, long agent_ref) {
        String s = "";
        if (name == null || "".equals(name)) {
            s = "Please Enter Name";
            return s;
        }
        if (phone == null || "".equals(phone)) {
            s = "Please Enter Phone Number";
            return s;
        }
        if ("".equals(doc)) {
            s = "Please Enter Docctor";
            return s;
        }
        if ("".equals(ses)) {
            s = "Please Enter Session";
            return s;
        }
        if ("".equals(agent)) {
            s = "Please Enter Agency";
            return s;
        }
        if ("".equals(agent_ref)) {
            s = "Please Enter Agency Reference No";
            return s;
        }
        if ("".equals(agent_ref)) {
            s = "Please Enter Agency Reference No";
            return s;
        }

        return s;
    }

    private Bill saveBilledBill(ServiceSession ss, String name, String phone, String doc, long agent, long agent_ref) {
        Bill savingBill = createBill(ss, name, phone, agent);
        BillItem savingBillItem = createBillItem(savingBill, agent_ref, ss);
        BillSession savingBillSession = createBillSession(savingBill, savingBillItem, ss);

        List<BillFee> savingBillFees = createBillFee(savingBill, savingBillItem, ss);
        List<BillItem> savingBillItems = new ArrayList<>();
        savingBillItems.add(savingBillItem);

        getAmount(ss);

        getBillItemFacade().edit(savingBillItem);

        //Update Bill Session
        savingBillItem.setHospitalFee(billBeanController.calFeeValue(FeeType.OwnInstitution, savingBillItem));
        savingBillItem.setStaffFee(billBeanController.calFeeValue(FeeType.Staff, savingBillItem));
        savingBillItem.setBillSession(savingBillSession);
        getBillSessionFacade().edit(savingBillSession);

        //Update Bill
        savingBill.setHospitalFee(billBeanController.calFeeValue(FeeType.OwnInstitution, savingBill));
        savingBill.setStaffFee(billBeanController.calFeeValue(FeeType.Staff, savingBill));
        savingBill.setSingleBillItem(savingBillItem);
        savingBill.setSingleBillSession(savingBillSession);
        savingBill.setBillItems(savingBillItems);
        savingBill.setBillFees(savingBillFees);

        if (savingBill.getBillType() == BillType.ChannelAgent) {
            updateBallance(savingBill.getCreditCompany(), 0 - savingBill.getNetTotal(), HistoryType.ChannelBooking, savingBill, savingBillItem, savingBillSession, savingBillItem.getAgentRefNo());
            savingBill.setBalance(0.0);
            savingBillSession.setPaidBillSession(savingBillSession);
        } else if (savingBill.getBillType() == BillType.ChannelCash) {
            savingBill.setBalance(0.0);
            savingBillSession.setPaidBillSession(savingBillSession);
        } else if (savingBill.getBillType() == BillType.ChannelOnCall) {
            savingBill.setBalance(savingBill.getNetTotal());
        } else if (savingBill.getBillType() == BillType.ChannelStaff) {
            savingBill.setBalance(savingBill.getNetTotal());
        }

        savingBill.setSingleBillItem(savingBillItem);
        savingBill.setSingleBillSession(savingBillSession);

        getBillFacade().edit(savingBill);
        getBillSessionFacade().edit(savingBillSession);
        return savingBill;
    }

    private Bill createBill(ServiceSession ss, String name, String phone, long agent) {
        Bill bill = new BilledBill();
        bill.setStaff(ss.getOriginatingSession().getStaff());
        bill.setAppointmentAt(ss.getSessionDate());
        bill.setTotal(getAmount(ss));
        bill.setNetTotal(getAmount(ss));
        bill.setPaymentMethod(PaymentMethod.Agent);

        Patient p = new Patient();
        p.setPerson(new Person());
        p.getPerson().setName(name);
        p.getPerson().setPhone(phone);
        getPersonFacade().create(p.getPerson());
        bill.setPatient(p);
        getPatientFacade().create(p);

        bill.setBillType(BillType.ChannelAgent);
        Institution institution = institutionFacade.find(agent);
        bill.setCreditCompany(institution);

        String insId = generateBillNumberInsId(bill, ss);

        if (insId.equals("")) {
            return null;
        }
        bill.setInsId(insId);

        String deptId = generateBillNumberDeptId(bill, ss);

        if (deptId.equals("")) {
            return null;
        }
        bill.setDeptId(deptId);

        if (bill.getBillType().getParent() == BillType.ChannelCashFlow) {
            bill.setBookingId(getBillNumberBean().bookingIdGenerator(ss.getInstitution(), new BilledBill()));
            bill.setPaidAmount(getAmount(ss));
            bill.setPaidAt(new Date());
        }

        bill.setBillDate(new Date());
        bill.setBillTime(new Date());
        bill.setCreatedAt(new Date());
//        bill.setCreater(null);
        bill.setDepartment(ss.getDepartment());
        bill.setInstitution(ss.getInstitution());

        bill.setToDepartment(ss.getDepartment());
        bill.setToInstitution(ss.getInstitution());

        getBillFacade().create(bill);

        if (bill.getBillType() == BillType.ChannelCash || bill.getBillType() == BillType.ChannelAgent) {
            System.out.println("paidBill 1= " + bill.getPaidBill());
            bill.setPaidBill(bill);
            getBillFacade().edit(bill);
        }

        return bill;
    }

    private BillItem createBillItem(Bill bill, long agent_ref, ServiceSession ss) {
        BillItem bi = new BillItem();
        bi.setAdjustedValue(0.0);
        bi.setAgentRefNo(String.valueOf(agent_ref));
        bi.setBill(bill);
        bi.setBillTime(new Date());
        bi.setCreatedAt(new Date());
        bi.setCreater(null);
        bi.setGrossValue(ss.getOriginatingSession().getTotal());
        bi.setItem(ss);
        bi.setNetRate(ss.getOriginatingSession().getTotal());
        bi.setNetValue(ss.getOriginatingSession().getTotal());
        bi.setQty(1.0);
        bi.setRate(ss.getOriginatingSession().getTotal());
        bi.setSessionDate(ss.getSessionAt());

        billItemFacade.create(bi);
        return bi;
    }

    private BillSession createBillSession(Bill bill, BillItem billItem, ServiceSession ss) {
        BillSession bs = new BillSession();
        bs.setAbsent(false);
        bs.setBill(bill);
        bs.setBillItem(billItem);
        bs.setCreatedAt(new Date());
        bs.setCreater(null);
        bs.setDepartment(ss.getOriginatingSession().getDepartment());
        bs.setInstitution(ss.getOriginatingSession().getInstitution());
        bs.setItem(ss);

        bs.setServiceSession(ss);
        bs.setSessionDate(ss.getSessionDate());
        bs.setSessionTime(ss.getSessionTime());
        bs.setStaff(ss.getStaff());

        int count = getServiceSessionBean().getSessionNumber(ss, ss.getSessionDate(), bs);
        System.err.println("count" + count);

        bs.setSerialNo(count);

        getBillSessionFacade().create(bs);

        return bs;
    }

    private List<BillFee> createBillFee(Bill bill, BillItem billItem, ServiceSession ss) {
        List<BillFee> billFeeList = new ArrayList<>();
        double tmpTotal = 0;
        double tmpDiscount = 0;
        System.out.println("ss.getOriginatingSession().getItemFees() = " + ss.getOriginatingSession().getItemFees().size());
        for (ItemFee f : ss.getOriginatingSession().getItemFees()) {
            if (bill.getPaymentMethod() != PaymentMethod.Agent) {
                if (f.getFeeType() == FeeType.OtherInstitution) {
                    continue;
                }
            }
            if (bill.getPaymentMethod() != PaymentMethod.OnCall) {
                if (f.getFeeType() == FeeType.OwnInstitution && f.getName().equalsIgnoreCase("On-Call Fee")) {
                    continue;
                }
            }
            BillFee bf = new BillFee();
            bf.setBill(bill);
            bf.setBillItem(billItem);
            bf.setCreatedAt(new Date());
//            bf.setCreater(null);
            if (f.getFeeType() == FeeType.OwnInstitution) {
                bf.setInstitution(f.getInstitution());
                bf.setDepartment(f.getDepartment());
            } else if (f.getFeeType() == FeeType.OtherInstitution) {
                bf.setInstitution(bill.getInstitution());
            } else if (f.getFeeType() == FeeType.Staff) {
                bf.setSpeciality(f.getSpeciality());
                System.out.println("bf.getSpeciality() = " + bf.getSpeciality());
                bf.setStaff(f.getStaff());
                System.out.println("bf.getStaff() = " + bf.getStaff());
            }

            bf.setFee(f);
            bf.setFeeAt(new Date());
            bf.setFeeDiscount(0.0);
            bf.setOrderNo(0);
            bf.setPatient(bill.getPatient());

            if (bf.getPatienEncounter() != null) {
                bf.setPatienEncounter(bill.getPatientEncounter());
            }

            bf.setPatient(bill.getPatient());

            if (f.getFeeType() == FeeType.Staff) {
                bf.setStaff(f.getStaff());
            }

            if (f.getFeeType() == FeeType.OwnInstitution) {
                bf.setInstitution(bill.getInstitution());
            }
            bf.setFeeValue(f.getFee());

            bf.setFeeGrossValue(bf.getFeeValue());
            bf.setFeeValue(bf.getFeeGrossValue() - bf.getFeeDiscount());

            tmpTotal += bf.getFeeValue();

            billFeeFacade.create(bf);
            billFeeList.add(bf);
        }
        bill.setTotal(tmpTotal);
        bill.setNetTotal(tmpTotal);
        System.out.println("tmpDiscount = " + tmpDiscount);
        System.out.println("tmpTotal = " + tmpTotal);
        System.out.println("bill.getNetTotal() = " + bill.getNetTotal());
        System.out.println("bill.getTotal() = " + bill.getTotal());
        getBillFacade().edit(bill);

        billItem.setDiscount(tmpDiscount);
        billItem.setNetValue(tmpTotal);
        System.out.println("billItem.getNetValue() = " + billItem.getNetValue());
        getBillItemFacade().edit(billItem);

        return billFeeList;

    }

    public double getAmount(ServiceSession ss) {
        double amount = 0.0;
        amount = ss.getOriginatingSession().getTotalFee();
        System.err.println("ss.getOriginatingSession().getTotalFee() = " + ss.getOriginatingSession().getTotalFee());

        return amount;
    }

    private double fetchLocalFee(Item item, PaymentMethod paymentMethod) {
        String jpql;
        Map m = new HashMap();
        FeeType[] fts = {FeeType.Service, FeeType.OwnInstitution, FeeType.Staff};
        List<FeeType> feeTypes = Arrays.asList(fts);
        jpql = "Select sum(f.fee)"
                + " from ItemFee f "
                + " where f.retired=false "
                + " and f.item=:ses ";

        if (paymentMethod == PaymentMethod.Agent) {
            FeeType[] fts1 = {FeeType.Service, FeeType.OwnInstitution, FeeType.Staff, FeeType.OtherInstitution};
            feeTypes = Arrays.asList(fts1);
            jpql += " and f.feeType in :fts1 "
                    + " and f.name!=:name";
            m.put("name", "On-Call Fee");
            m.put("fts1", feeTypes);
        } else {
            if (paymentMethod == PaymentMethod.OnCall) {
                jpql += " and f.feeType in :fts2 ";
                m.put("fts2", feeTypes);
            } else {
                jpql += " and f.feeType in :fts3 "
                        + " and f.name!=:name";
                m.put("name", "On-Call Fee");
                m.put("fts3", feeTypes);
            }
        }
        m.put("ses", item);
//        System.out.println("paymentMethod = " + paymentMethod);
//        System.out.println("feeTypes = " + feeTypes);
//        System.out.println("m = " + m);
        Double obj = getItemFeeFacade().findDoubleByJpql(jpql, m);

        if (obj == null) {
            return 0;
        }

        return obj;
    }

    private double fetchForiegnFee(Item item, PaymentMethod paymentMethod) {
        String jpql;
        Map m = new HashMap();
        FeeType[] fts = {FeeType.Service, FeeType.OwnInstitution, FeeType.Staff};
        List<FeeType> feeTypes = Arrays.asList(fts);
        jpql = "Select sum(f.ffee)"
                + " from ItemFee f "
                + " where f.retired=false "
                + " and f.item=:ses ";

        if (paymentMethod == PaymentMethod.Agent) {
            FeeType[] fts1 = {FeeType.Service, FeeType.OwnInstitution, FeeType.Staff, FeeType.OtherInstitution};
            feeTypes = Arrays.asList(fts1);
            jpql += " and f.feeType in :fts1 "
                    + " and f.name!=:name";
            m.put("name", "On-Call Fee");
            m.put("fts1", feeTypes);
        } else {
            if (paymentMethod == PaymentMethod.OnCall) {
                jpql += " and f.feeType in :fts2 ";
                m.put("fts2", feeTypes);
            } else {
                jpql += " and f.feeType in :fts3 "
                        + " and f.name!=:name";
                m.put("name", "On-Call Fee");
                m.put("fts3", feeTypes);
            }
        }
        m.put("ses", item);
//        System.out.println("paymentMethod = " + paymentMethod);
//        System.out.println("feeTypes = " + feeTypes);
//        System.out.println("m = " + m);
        Double obj = getItemFeeFacade().findDoubleByJpql(jpql, m);

        if (obj == null) {
            return 0;
        }

        return obj;
    }

    private List<ItemFee> fetchFee(Item item) {
        String jpql;
        Map m = new HashMap();
        jpql = "Select f "
                + " from ItemFee f "
                + " where f.retired=false "
                + " and f.item=:ses ";
        m.put("ses", item);
        List<ItemFee> list = getItemFeeFacade().findBySQL(jpql, m, TemporalType.TIMESTAMP);
//        System.err.println("Fetch Fess " + list.size());
        return list;
    }

    private String generateBillNumberInsId(Bill bill, ServiceSession ss) {
        String suffix = ss.getInstitution().getInstitutionCode();
        BillClassType billClassType = null;
        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);
        BillType billType = null;
        String insId = null;
        if (bill instanceof BilledBill) {

            billClassType = BillClassType.BilledBill;
            if (bill.getBillType() == BillType.ChannelOnCall || bill.getBillType() == BillType.ChannelStaff) {
                billType = bill.getBillType();
                if (billType == BillType.ChannelOnCall) {
                    suffix += "BKONCALL";
                } else {
                    suffix += "BKSTAFF";
                }
                insId = getBillNumberBean().institutionBillNumberGenerator(ss.getInstitution(), billType, billClassType, suffix);
            } else {
                suffix += "CHANN";
                insId = getBillNumberBean().institutionBillNumberGenerator(ss.getInstitution(), bts, billClassType, suffix);
            }
        }

        if (bill instanceof CancelledBill) {
            suffix += "CHANNCAN";
            billClassType = BillClassType.CancelledBill;
            insId = getBillNumberBean().institutionBillNumberGenerator(ss.getInstitution(), bts, billClassType, suffix);
        }

        if (bill instanceof RefundBill) {
            suffix += "CHANNREF";
            billClassType = BillClassType.RefundBill;
            insId = getBillNumberBean().institutionBillNumberGenerator(ss.getInstitution(), bts, billClassType, suffix);
        }

        System.out.println("billClassType = " + billClassType);
        System.out.println("insId = " + insId);

        return insId;
    }

    private String generateBillNumberDeptId(Bill bill, ServiceSession ss) {
        String suffix = ss.getDepartment().getDepartmentCode();
        BillClassType billClassType = null;
        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);
        BillType billType = null;
        String deptId = null;
        if (bill instanceof BilledBill) {

            billClassType = BillClassType.BilledBill;
            if (bill.getBillType() == BillType.ChannelOnCall || bill.getBillType() == BillType.ChannelStaff) {
                billType = bill.getBillType();
                if (billType == BillType.ChannelOnCall) {
                    suffix += "BKONCALL";
                } else {
                    suffix += "BKSTAFF";
                }
                deptId = getBillNumberBean().departmentBillNumberGenerator(ss.getInstitution(), ss.getDepartment(), billType, billClassType, suffix);
            } else {
                suffix += "CHANN";
                deptId = getBillNumberBean().departmentBillNumberGenerator(ss.getInstitution(), ss.getDepartment(), bts, billClassType, suffix);
            }
        }

        if (bill instanceof CancelledBill) {
            suffix += "CHANNCAN";
            billClassType = BillClassType.CancelledBill;
            deptId = getBillNumberBean().departmentBillNumberGenerator(ss.getInstitution(), ss.getDepartment(), bts, billClassType, suffix);
        }

        if (bill instanceof RefundBill) {
            suffix += "CHANNREF";
            billClassType = BillClassType.RefundBill;
            deptId = getBillNumberBean().departmentBillNumberGenerator(ss.getInstitution(), ss.getDepartment(), bts, billClassType, suffix);
        }

        System.out.println("billClassType = " + billClassType);
        System.out.println("deptId = " + deptId);

        return deptId;
    }

    public void updateBallance(Institution ins, double transactionValue, HistoryType historyType, Bill bill, BillItem billItem, BillSession billSession, String refNo) {
        System.out.println("updating agency balance");
        System.out.println("ins.getName() = " + ins.getName());
        System.out.println("ins.getBallance() before " + ins.getBallance());
        System.out.println("transactionValue = " + transactionValue);
        AgentHistory agentHistory = new AgentHistory();
        agentHistory.setCreatedAt(new Date());
//        agentHistory.setCreater(null);
        agentHistory.setBill(bill);
        agentHistory.setBillItem(billItem);
        agentHistory.setBillSession(billSession);
        agentHistory.setBeforeBallance(ins.getBallance());
        agentHistory.setTransactionValue(transactionValue);
        agentHistory.setReferenceNo(refNo);
        agentHistory.setHistoryType(historyType);
        agentHistoryFacade.create(agentHistory);

        ins.setBallance(ins.getBallance() + transactionValue);
        getInstitutionFacade().edit(ins);

    }

    /**
     * PUT method for updating or creating an instance of Api
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }

    public StaffFacade getStaffFacade() {
        return staffFacade;
    }

    public void setStaffFacade(StaffFacade staffFacade) {
        this.staffFacade = staffFacade;
    }

    public ItemFeeFacade getItemFeeFacade() {
        return ItemFeeFacade;
    }

    public void setItemFeeFacade(ItemFeeFacade ItemFeeFacade) {
        this.ItemFeeFacade = ItemFeeFacade;
    }

    public BillSessionFacade getBillSessionFacade() {
        return billSessionFacade;
    }

    public void setBillSessionFacade(BillSessionFacade billSessionFacade) {
        this.billSessionFacade = billSessionFacade;
    }

    public ServiceSessionFacade getServiceSessionFacade() {
        return serviceSessionFacade;
    }

    public void setServiceSessionFacade(ServiceSessionFacade serviceSessionFacade) {
        this.serviceSessionFacade = serviceSessionFacade;
    }

    public InstitutionFacade getInstitutionFacade() {
        return institutionFacade;
    }

    public void setInstitutionFacade(InstitutionFacade institutionFacade) {
        this.institutionFacade = institutionFacade;
    }

    public CommonFunctions getCommonFunctions() {
        return commonFunctions;
    }

    public void setCommonFunctions(CommonFunctions commonFunctions) {
        this.commonFunctions = commonFunctions;
    }

    public ChannelBean getChannelBean() {
        return channelBean;
    }

    public void setChannelBean(ChannelBean channelBean) {
        this.channelBean = channelBean;
    }

    public BillNumberGenerator getBillNumberBean() {
        return billNumberBean;
    }

    public void setBillNumberBean(BillNumberGenerator billNumberBean) {
        this.billNumberBean = billNumberBean;
    }

    public BillFacade getBillFacade() {
        return billFacade;
    }

    public void setBillFacade(BillFacade billFacade) {
        this.billFacade = billFacade;
    }

    public UriInfo getContext() {
        return context;
    }

    public void setContext(UriInfo context) {
        this.context = context;
    }

    public BillItemFacade getBillItemFacade() {
        return billItemFacade;
    }

    public void setBillItemFacade(BillItemFacade billItemFacade) {
        this.billItemFacade = billItemFacade;
    }

    public ServiceSessionBean getServiceSessionBean() {
        return serviceSessionBean;
    }

    public void setServiceSessionBean(ServiceSessionBean serviceSessionBean) {
        this.serviceSessionBean = serviceSessionBean;
    }

    public BillFeeFacade getBillFeeFacade() {
        return billFeeFacade;
    }

    public void setBillFeeFacade(BillFeeFacade billFeeFacade) {
        this.billFeeFacade = billFeeFacade;
    }

    public AgentHistoryFacade getAgentHistoryFacade() {
        return agentHistoryFacade;
    }

    public void setAgentHistoryFacade(AgentHistoryFacade agentHistoryFacade) {
        this.agentHistoryFacade = agentHistoryFacade;
    }

    public PatientFacade getPatientFacade() {
        return patientFacade;
    }

    public void setPatientFacade(PatientFacade patientFacade) {
        this.patientFacade = patientFacade;
    }

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    public BillBeanController getBillBeanController() {
        return billBeanController;
    }

    public void setBillBeanController(BillBeanController billBeanController) {
        this.billBeanController = billBeanController;
    }

    public CommonController getCommonController() {
        return commonController;
    }

    public void setCommonController(CommonController commonController) {
        this.commonController = commonController;
    }

}