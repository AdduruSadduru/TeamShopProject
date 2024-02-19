package com.shop.controller;

import com.shop.dto.MemberASDto;
import com.shop.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final MemberService memberService;
    private final HttpSession httpSession;
    @GetMapping(value = "/myInfoAS")
    public String infoAS(Principal principal, Model model) {
        MemberASDto memberASDto = MemberASDto.of(memberService.findMember(httpSession, principal));
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        model.addAttribute("memberASDto", memberASDto);
        return "member/memberProfile";
    }

    @PostMapping(value = "/myInfoAS")
    public String infoUpdate(@Valid MemberASDto memberASDto, BindingResult bindingResult,
                             Principal principal, Model model) {
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        if (bindingResult.hasErrors()) {
            return "member/memberProfile";
        }
        memberService.memberAS(memberASDto);
        return "redirect:/";
    }
}
