package com.skykimpro.chingu.settings;

import com.skykimpro.chingu.WithAccount;
import com.skykimpro.chingu.account.AccountRepository;
import com.skykimpro.chingu.account.AccountService;
import com.skykimpro.chingu.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountRepository accountRepository;

    @Autowired PasswordEncoder passwordEncoder;

    @AfterEach
    void afterEach(){
        accountRepository.deleteAll();
    }

    @WithAccount("KJA")
    @DisplayName("프로필 수정 폼")
    @Test
    void updateProfileForm() throws Exception {
        String bio = "짧은 소개를 수정하는  경우.";
        mockMvc.perform(get(SettingsController.SETTING_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @WithAccount("KJA")
    @DisplayName("프로필 수정하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception {
        String bio = "짧은 소개를 수정하는  경우.";
        mockMvc.perform(post(SettingsController.SETTING_PROFILE_URL)
                        .param("bio", bio)
                        .with(csrf()))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl(SettingsController.SETTING_PROFILE_URL))
                        .andExpect(flash().attributeExists("message"));

        Account kja = accountRepository.findByNickname("KJA");
        assertEquals(bio, kja.getBio());
    }

    @WithAccount("KJA")
    @DisplayName("프로필 수정하기 - 입력값 에러")
    @Test
    void updateProfile_error() throws Exception {
        String bio = "길게 소개를 수정하는  경우.길게 소개를 수정하는  경우.길게 소개를 수정하는  경우.길게 소개를 수정하는  경우.길게 소개를 수정하는  경우.";
        mockMvc.perform(post(SettingsController.SETTING_PROFILE_URL)
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTING_PROFILE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account kja = accountRepository.findByNickname("KJA");
        assertNotEquals(bio, kja.getBio());
    }

    @WithAccount("KJA")
    @DisplayName("패스워드 수정 폼")
    @Test
    void updatePassword_form() throws Exception {
        mockMvc.perform(get(SettingsController.SETTING_PASSWORD_URL))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("account"))
                    .andExpect(model().attributeExists("passwordForm"));
    }

    @WithAccount("KJA")
    @DisplayName("패스워드 수정 - 입력값 정상")
    @Test
    void updatePassword_success() throws Exception {
        mockMvc.perform(post(SettingsController.SETTING_PASSWORD_URL)
                .param("newPassword", "12345678")
                .param("newPasswordConfirm", "12345678")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTING_PASSWORD_URL))
                .andExpect(flash().attributeExists("message"));

        Account kja = accountRepository.findByNickname("KJA");
        assertTrue(passwordEncoder.matches("12345678", kja.getPassword()));
    }

    @WithAccount("KJA")
    @DisplayName("패스워드 수정 - 입력값 에러 - 패스워드 불일치")
    @Test
    void updatePassword_fail() throws Exception {
        mockMvc.perform(post(SettingsController.SETTING_PASSWORD_URL)
                .param("newPassword", "12345678")
                .param("newPasswordConfirm", "11111111")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTING_PASSWORD_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(model().hasErrors());

        Account kja = accountRepository.findByNickname("KJA");
        assertNotEquals("11111111", kja.getPassword());
    }

    @WithAccount("KJA")
    @DisplayName("닉네임 수정 폼")
    @Test
    void updateAccountForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTING_ACCOUNT_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));
    }

    @WithAccount("KJA")
    @DisplayName("닉네임 수정하기 - 입력값 정상")
    @Test
    void updateAccount_success() throws Exception {
        String newNickname = "newnickname1234";
        mockMvc.perform(post(SettingsController.SETTING_ACCOUNT_URL)
                .param("nickname", newNickname)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTING_ACCOUNT_URL))
                .andExpect(flash().attributeExists("message"));

        assertNotNull(accountRepository.findByNickname("newnickname1234"));
    }

    @WithAccount("KJA")
    @DisplayName("닉네임 수정하기 - 입력값 에러 - 패턴 맞지 않음")
    @Test
    void updateAccount_fail() throws Exception {
        mockMvc.perform(post(SettingsController.SETTING_ACCOUNT_URL)
                .param("nickname", "ABC!@#$%^&*()")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTING_ACCOUNT_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"))
                .andExpect(model().hasErrors());
    }
}