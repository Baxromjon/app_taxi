package ai.ecma.server.service;

import ai.ecma.server.entity.*;
import ai.ecma.server.entity.enums.PayTypeEnum;
import ai.ecma.server.exceptions.ResourceNotFoundException;
import ai.ecma.server.payload.PageableDto;
import ai.ecma.server.payload.PaymentDto;
import ai.ecma.server.payload.Result;
import ai.ecma.server.repository.CarRepository;
import ai.ecma.server.repository.PayTypeRepository;
import ai.ecma.server.repository.PaymentRepository;
import ai.ecma.server.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * BY BAXROMJON on 05.11.2020
 */

@Service
public class PaymentService {
    @Autowired
    CarRepository carRepository;
    @Autowired
    PayTypeRepository payTypeRepository;
    @Autowired
    PaymentRepository paymentRepository;

    /**
     * TO'LOV QO'SHISH UCHUN. BU METHODNI FAQAT TO'LOV TURLARI ISHLATA OLADI
     *
     * @param paymentDto
     * @param user
     * @return
     */
    public Result addPayment(PaymentDto paymentDto, User user) {
        try {
            Car car = carRepository.findByDriverPhoneNumber(paymentDto.getPhoneNumber()).orElseThrow(() -> new ResourceNotFoundException("car", "driverPhoneNumber", paymentDto.getPhoneNumber()));
            Payment payment = new Payment(
                    car,
                    paymentDto.getPaySum(),
                    getPayTypeByCurrentUser(user));
            paymentRepository.save(payment);
            return new Result("Paid", true);
        } catch (Exception e) {
            return new Result("Xatolik", false);
        }
    }

    /**
     * PAYMENT DTO LARNI QAYTARADIGAN PAGEABLE HOLATDA
     *
     * @param page
     * @param size
     * @param begin
     * @param end
     * @return
     */


    public PageableDto getPayments(int page, int size, boolean asc,
                                   String sortType, Timestamp begin, Timestamp end) {
        Pageable pageable = CommonUtils.getPageable(page, size, asc, sortType);
        Page<Payment> paymentPage = paymentRepository.findAllByCreatedAtBetween(begin, end, pageable);
        List<Payment> paymentList = paymentPage.getContent();
        long totalElements = paymentPage.getTotalElements();
        int totalPages = paymentPage.getTotalPages();

        return new PageableDto(
                getPaymentDtoList(paymentList),
                totalElements,
                totalPages,
                size,
                page);
    }


    public List<PaymentDto> getPaymentDtoList(List<Payment> payments) {
        return payments.stream().map(this::getPaymentDto).collect(Collectors.toList());
    }


    public PaymentDto getPaymentDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                getUserFullName(payment.getCar().getDriver()),
                payment.getCar().getRegStateNumber(),
                payment.getPayType().getName(),
                payment.getPaySum(),
                payment.getCreatedAt());
    }


    /**
     * BU KIRGAN ODAMIGA QARAB TO'LOV TURINI QAYTARADI
     *
     * @param user
     * @return
     */
    private PayType getPayTypeByCurrentUser(User user) {
        Set<Role> roles = user.getRoles();
        String authority = null;
        for (Role role : roles) {
            authority = role.getAuthority();
            break;
        }
        String payTypeEnum = Objects.requireNonNull(authority).substring(authority.indexOf("ROLE_") + 5);
        return payTypeRepository.findByPayTypeEnum(PayTypeEnum.valueOf(payTypeEnum)).orElseThrow(() -> new ResourceNotFoundException("getPayType", "payTypeEnum", payTypeEnum));
    }

    public static String getUserFullName(User user) {
        String fullName = (user.getFirstName() == null ? "" : (user.getFirstName() + " "))
                + (user.getLastName() == null ? "" : (user.getLastName() + " "))
                + (user.getMiddleName() == null ? "" : user.getMiddleName());
        return fullName.isEmpty() ? "Noma'lum" : fullName;
    }
}
