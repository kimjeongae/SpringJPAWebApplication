package com.skykimpro.chingu.settings;

import com.skykimpro.chingu.account.AccountService;
import com.skykimpro.chingu.account.CurrentUser;
import com.skykimpro.chingu.domain.Account;
import com.skykimpro.chingu.settings.form.NicknameForm;
import com.skykimpro.chingu.settings.form.Notifications;
import com.skykimpro.chingu.settings.form.PasswordForm;
import com.skykimpro.chingu.settings.form.Profile;
import com.skykimpro.chingu.settings.validator.NicknameValidator;
import com.skykimpro.chingu.settings.validator.PasswordFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    static final String SETTING_PROFILE_VIEW_NAME = "settings/profile";
    static final String SETTING_PROFILE_URL = "/" + SETTING_PROFILE_VIEW_NAME;
    static final String SETTING_PASSWORD_VIEW_NAME = "settings/password";
    static final String SETTING_PASSWORD_URL = "/" + SETTING_PASSWORD_VIEW_NAME;
    static final String SETTING_NOTIFICATIONS_VIEW_NAME = "settings/notifications";
    static final String SETTING_NOTIFICATIONS_URL = "/" + SETTING_NOTIFICATIONS_VIEW_NAME;
    static final String SETTING_ACCOUNT_VIEW_NAME = "settings/account";
    static final String SETTING_ACCOUNT_URL = "/" + SETTING_ACCOUNT_VIEW_NAME;

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final NicknameValidator nicknameValidator;

    @InitBinder("passwordForm")
    public void passwordFormInitBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder("nicknameForm")
    public void nicknameInitBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(nicknameValidator);
    }

    @GetMapping(SETTING_PROFILE_URL)
    public String profileUpdateForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Profile.class));
        return SETTING_PROFILE_VIEW_NAME;
    }

    @PostMapping(SETTING_PROFILE_URL)
    public String updateProfileForm(@CurrentUser Account account, @Valid Profile profile, Errors errors,
                                Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTING_PROFILE_VIEW_NAME;
        }

        accountService.updateProfile(account, profile);
        attributes.addFlashAttribute("message","프로필을 수정했습니다.");
        return "redirect:" + SETTING_PROFILE_URL;
    }

    @GetMapping(SETTING_PASSWORD_URL)
    public String updatePasswordForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return SETTING_PASSWORD_VIEW_NAME;
    }

    @PostMapping(SETTING_PASSWORD_URL)
    public String updatePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors,
                                     Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTING_PASSWORD_VIEW_NAME;
        }

        accountService.updatePassword(account, passwordForm.getNewPassword());
        attributes.addFlashAttribute("message","비밀번호를 수정했습니다.");
        return "redirect:" + SETTING_PASSWORD_URL;
    }

    @GetMapping(SETTING_NOTIFICATIONS_URL)
    public String updateNotificatonForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Notifications.class));
        return SETTING_NOTIFICATIONS_VIEW_NAME;
    }

    @PostMapping(SETTING_NOTIFICATIONS_URL)
    public String updateNotificaton(@CurrentUser Account account, @Valid Notifications notifications, Errors errors,
                                     Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTING_NOTIFICATIONS_VIEW_NAME;
        }

        accountService.updateNotificatons(account, notifications);
        attributes.addFlashAttribute("message","알림 설정을 수정했습니다.");
        return "redirect:" + SETTING_NOTIFICATIONS_URL;
    }

    @GetMapping(SETTING_ACCOUNT_URL)
    public String updateAccountForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, NicknameForm.class));
        return SETTING_ACCOUNT_VIEW_NAME;
    }

    @PostMapping(SETTING_ACCOUNT_URL)
    public String updateAccount(@CurrentUser Account account, @Valid NicknameForm nicknameForm, Errors errors,
                                    Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTING_ACCOUNT_VIEW_NAME;
        }

        accountService.updateNickname(account, nicknameForm.getNickname());
        attributes.addFlashAttribute("message","닉네임을 수정했습니다.");
        return "redirect:" + SETTING_ACCOUNT_URL;
    }
}
