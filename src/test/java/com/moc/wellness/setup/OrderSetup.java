package com.moc.wellness.setup;

import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.dto.exercise.ExerciseResponse;
import com.moc.wellness.dto.order.OrderBody;
import com.moc.wellness.dto.order.OrderResponse;
import com.moc.wellness.dto.order.PriceDto;
import com.moc.wellness.dto.training.TrainingResponse;
import com.moc.wellness.enums.Role;
import com.moc.wellness.model.Exercise;
import com.moc.wellness.model.Order;
import com.moc.wellness.model.Training;
import com.moc.wellness.model.user.UserCustom;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class OrderSetup {
    protected final Long adminId = 1L;
    protected final Long trainerId = 2L;
    protected final Long userId = 3L;


    protected final Long trainingId = 1L;
    protected final Long exerciseId = 1L;
    protected final Long orderId = 1L;

    protected UserCustom userTrainer;
    protected UserCustom userAdmin;
    protected UserCustom userUser;

    protected UserDto trainerDto;
    protected UserDto adminDto;

    protected UserDto userDto;
    protected Training training;

    protected Exercise exercise;

    protected Order order;

    protected ExerciseResponse exerciseResponse;
    protected TrainingResponse trainingResponse;
    protected OrderResponse orderResponse;

    protected OrderBody orderBody;

    protected PageableResponse<List<OrderResponse>> pageableResponse;

    protected PageableBody pageableBody;

    protected final int page = 0;
    protected final int size = 10;

    protected final double price = 10;
    protected PageRequest pageRequest;

    protected PriceDto priceDto;

    @BeforeEach
    public void setUp() {
        setUserTrainer(UserCustom.builder()
                .id(getTrainerId())
                .email("marcel@gmail.com")
                .firstName("Marcel")
                .lastName("Popescu")
                .password("1234")
                .role(Role.TRAINER)
                .build());
        setUserAdmin(UserCustom.builder()
                .id(getAdminId())
                .email("Admin@gmail.com")
                .firstName("Razvan")
                .lastName("Mocica")
                .password("1234")
                .role(Role.ADMIN)
                .build());

        setTrainerDto(UserDto.builder()
                .id(getTrainerId())
                .email("marcel@gmail.com")
                .firstName("Marcel")
                .lastName("Popescu")
                .role(Role.TRAINER)
                .build());
        setAdminDto(UserDto.builder()
                .id(getAdminId())
                .email("Admin@gmail.com")
                .firstName("Razvan")
                .lastName("Mocica")
                .role(Role.ADMIN)
                .build());
        setUserUser(UserCustom.builder()
                .id(getUserId())
                .email("user@gmail.com")
                .firstName("User")
                .lastName("user")
                .password("1234")
                .role(Role.USER)
                .build());
        setUserDto(UserDto.builder()
                .id(getUserId())
                .email("user@gmail.com")
                .firstName("User")
                .lastName("user")
                .role(Role.USER)
                .build());


        setExercise(Exercise.builder()
                .id(getExerciseId())
                .muscleGroups(List.of("bicep"))
                .title("test")
                .body("test")
                .userLikes(Set.of(getUserAdmin(), getUserTrainer()))
                .userDislikes(new HashSet<>())
                .user(getUserTrainer())
                .approved(true)
                .build());

        setTraining(Training.builder()
                .id(getTrainingId())
                .user(getUserTrainer())
                .title("test")
                .body("test")
                .userDislikes(Set.of(getUserUser()))
                .userLikes(Set.of(getUserAdmin(), getUserTrainer()))
                .exercises(Set.of(getExercise()))
                .price(getPrice())
                .approved(true)
                .build());
        setOrder(Order.builder()
                .id(getOrderId())
                .payed(false)
                .user(getUserUser())
                .trainings(Set.of(getTraining()))
                .shippingAddress("address")
                .build());

        setExerciseResponse(ExerciseResponse.builder()
                .muscleGroups(getExercise().getMuscleGroups())
                .body(getExercise().getBody())
                .title(getExercise().getTitle())
                .userLikes(Set.of(getAdminDto(), getTrainerDto()))
                .userDislikes(new HashSet<>())
                .id(getExerciseId())
                .user(getTrainerDto())
                .build());


        setTrainingResponse(TrainingResponse.builder()
                .price(getTraining().getPrice())
                .exercises(Set.of(getExerciseResponse()))
                .approved(true)
                .body(getTraining().getBody())
                .title(getTraining().getTitle())
                .userDislikes(Set.of(getUserDto()))
                .userLikes(Set.of(getAdminDto(), getTrainerDto()))
                .id(getTrainingId())
                .user(getTrainerDto())
                .build());

        setOrderResponse(OrderResponse.builder()
                .trainings(Set.of(getTrainingResponse()))
                .shippingAddress(getOrder().getShippingAddress())
                .payed(getOrder().isPayed())
                .id(getOrderId())
                .user(getUserDto())
                .build());

        setPageableResponse(PageableResponse.<List<OrderResponse>>builder()
                .totalElements(1)
                .totalPages(1)
                .payload(List.of(getOrderResponse()))
                .build());

        setPageableBody(PageableBody.builder()
                .size(getSize())
                .page(getPage())
                .build());
        setPageRequest(PageRequest.of(getPage(), getSize()));
        setPriceDto(PriceDto.builder().price(getPrice()).build());
        setOrderBody(OrderBody.builder()
                .shippingAddress(getOrder().getShippingAddress())
                .payed(false)
                .trainings(Set.of(getTrainingId()))
                .build());
    }

    protected Long getAdminId() {
        return adminId;
    }

    protected Long getTrainerId() {
        return trainerId;
    }

    protected Long getUserId() {
        return userId;
    }

    protected Long getTrainingId() {
        return trainingId;
    }

    protected Long getExerciseId() {
        return exerciseId;
    }

    protected Long getOrderId() {
        return orderId;
    }

    protected UserCustom getUserTrainer() {
        return userTrainer;
    }

    protected void setUserTrainer(UserCustom userTrainer) {
        this.userTrainer = userTrainer;
    }

    protected UserCustom getUserAdmin() {
        return userAdmin;
    }

    protected void setUserAdmin(UserCustom userAdmin) {
        this.userAdmin = userAdmin;
    }

    protected UserCustom getUserUser() {
        return userUser;
    }

    protected void setUserUser(UserCustom userUser) {
        this.userUser = userUser;
    }

    protected UserDto getTrainerDto() {
        return trainerDto;
    }

    protected void setTrainerDto(UserDto trainerDto) {
        this.trainerDto = trainerDto;
    }

    protected UserDto getAdminDto() {
        return adminDto;
    }

    protected void setAdminDto(UserDto adminDto) {
        this.adminDto = adminDto;
    }

    protected UserDto getUserDto() {
        return userDto;
    }

    protected void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    protected Training getTraining() {
        return training;
    }

    protected void setTraining(Training training) {
        this.training = training;
    }

    protected Exercise getExercise() {
        return exercise;
    }

    protected void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    protected Order getOrder() {
        return order;
    }

    protected void setOrder(Order order) {
        this.order = order;
    }

    protected ExerciseResponse getExerciseResponse() {
        return exerciseResponse;
    }

    protected void setExerciseResponse(ExerciseResponse exerciseResponse) {
        this.exerciseResponse = exerciseResponse;
    }

    protected TrainingResponse getTrainingResponse() {
        return trainingResponse;
    }

    protected void setTrainingResponse(TrainingResponse trainingResponse) {
        this.trainingResponse = trainingResponse;
    }

    protected OrderResponse getOrderResponse() {
        return orderResponse;
    }

    protected void setOrderResponse(OrderResponse orderResponse) {
        this.orderResponse = orderResponse;
    }

    protected OrderBody getOrderBody() {
        return orderBody;
    }

    protected void setOrderBody(OrderBody orderBody) {
        this.orderBody = orderBody;
    }

    protected PageableResponse<List<OrderResponse>> getPageableResponse() {
        return pageableResponse;
    }

    protected void setPageableResponse(PageableResponse<List<OrderResponse>> pageableResponse) {
        this.pageableResponse = pageableResponse;
    }

    protected PageableBody getPageableBody() {
        return pageableBody;
    }

    protected void setPageableBody(PageableBody pageableBody) {
        this.pageableBody = pageableBody;
    }

    protected int getPage() {
        return page;
    }

    protected int getSize() {
        return size;
    }

    protected double getPrice() {
        return price;
    }

    protected PageRequest getPageRequest() {
        return pageRequest;
    }

    protected void setPageRequest(PageRequest pageRequest) {
        this.pageRequest = pageRequest;
    }

    protected PriceDto getPriceDto() {
        return priceDto;
    }

    protected void setPriceDto(PriceDto priceDto) {
        this.priceDto = priceDto;
    }
}
