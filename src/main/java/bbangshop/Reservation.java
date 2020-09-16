package bbangshop;

import javax.persistence.*;

import org.springframework.beans.BeanUtils;

@Entity
@Table(name="Reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long reservationId;
    private Long breadId;
    private String status;

    /**
     * 예약접수를 신청하면 상품수량을 확인하고 예약가능/불가여부를 판단
     * */
    @PrePersist
    public void onPrePersist(){
        //bbangshop.external.Bread bread = new bbangshop.external.Bread();

        String checkQuantity = ReservationApplication.applicationContext.getBean(bbangshop.external.BreadService.class).checkBreadQuantity(this.getBreadId().toString());

        if(Integer.parseInt(checkQuantity) > 0){
            this.setStatus("breadRequested");
        }else{
            this.setStatus("soldout");
        }
    }
    /**
     * breadRequested 가능이면 배정관리서비스로 예약번호 전송
     * */
    @PostPersist
    public void onPostPersist(){
        ReservationRequested reservationRequested = new ReservationRequested();
        BeanUtils.copyProperties(this, reservationRequested);
        if("breadRequested".equals(this.getStatus())) reservationRequested.publishAfterCommit();
    }
    /**
     * cancellation  이 후 상품재고 원복 및 배정정보 삭제 이벤트 전달
     * */
    @PostUpdate
    public  void onPostUpdate(){
        if("cancellation".equals(this.getStatus())){
            ReservationCancelRequested reservationCancelRequested = new ReservationCancelRequested();
            BeanUtils.copyProperties(this, reservationCancelRequested);
            reservationCancelRequested.publishAfterCommit();
        }
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
    public Long getBreadId() {
        return breadId;
    }

    public void setBreadId(Long breadId) {
        this.breadId = breadId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }




}
