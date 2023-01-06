package vttp2022.paf.assessment.eshop.respositories;

public class Queries {

    final static String SQL_FIND_CUSTOMER_BY_NAME="SELECT * FROM eshop.customers where name = ?;";


    // Insert method
    final static String SQL_INSERT_NEW_ORDER_DETAILS="insert into orders ( order_id, order_date, name) values ( ?,?,?)";
    final static String SQL_INSERT_NEW_ITEM_DETAILS="insert into item ( item,quantity, order_id) values(?,?,?)";
    final static String SQL_INSERT_NEW_ORDER_STATUS_DETAILS="insert into order_status(order_id, delivery_id, status, status_update) values (?,? ,?, ?);";

    //Get order_status and user method
    final static String SQL_GET__ORDER_STATUS_FOR_USER ="select o.name,count(os.status),os.status from  orders as o left join order_status as os on o.order_id=os.order_id where o.name = ? group by os.status;";


}
