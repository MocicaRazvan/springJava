package com.moc.wellness.setup;

import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.UserBody;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.enums.Role;
import com.moc.wellness.model.user.UserCustom;
import org.junit.jupiter.api.BeforeEach;

public abstract class UserSetup {

    protected final Long adminId = 1L;
    protected final Long trainerId = 2L;
    protected final Long userId = 3L;

    protected UserCustom userTrainer;
    protected UserCustom userAdmin;
    protected UserCustom userUser;

    protected UserDto trainerDto;
    protected UserDto adminDto;

    protected UserDto userDto;

    protected UserBody userBody;

    protected final int page = 0;
    protected final int size = 10;
    protected PageableBody pageableBody;


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


        setUserBody(UserBody.builder()
                .lastName("changed")
                .firstName("changed")
                .email("changed@gmail.com")
                .build());

        pageableBody = PageableBody.builder()
                .page(page)
                .size(size)
                .build();
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

    protected UserBody getUserBody() {
        return userBody;
    }

    protected void setUserBody(UserBody userBody) {
        this.userBody = userBody;
    }
}
