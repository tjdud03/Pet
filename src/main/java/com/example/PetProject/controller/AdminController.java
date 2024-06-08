package com.example.PetProject.controller;

import com.example.PetProject.domain.*;
import com.example.PetProject.repository.FaqRepository;
import com.example.PetProject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class AdminController {
    private final FaqService faqService;
    private final QuestionService questionService;
    private final BannerService bannerService;
    private final FaqRepository faqRepository;
    private final BreedService breedService;
    private final MemberService memberService;

    // 생성자를 통한 의존성 주입
    @Autowired
    public AdminController(FaqService faqService, QuestionService questionService, BannerService bannerService, FaqRepository faqRepository, BreedService breedService, MemberService memberService) {
        this.faqService = faqService;
        this.questionService = questionService;
        this.bannerService = bannerService;
        this.faqRepository = faqRepository;
        this.breedService = breedService;
        this.memberService = memberService;
    }

    @GetMapping("/faq")
    public String faqpage(Model model) {
        List<FAQ> faq_list = faqService.getList();
        model.addAttribute("faq_list", faq_list);
        return "faq_admin";
    }

    @GetMapping("/question")
    public String questionpage(Model model) {
        List<Question> question_list = questionService.getList();
        model.addAttribute("question_list", question_list);
        return "question_admin";
    }

    @GetMapping("/banner")
    public String bannerpage(Model model) {
        List<Banner> banner_list = bannerService.getList();
        model.addAttribute("banner_list", banner_list);
        return "banner_admin";
    }

    @GetMapping("/breed")
    public String breedpage(Model model){
        List<Breed> breed_list = breedService.getReportedAndDeletedBreeds();
        model.addAttribute("breed_list", breed_list);
        return "breed";
    }

    @GetMapping("/breed/view/{id}")
    public String viewBreed(@PathVariable("id") Integer id, Model model) {
        Breed breed = breedService.getBreedById(id);
        model.addAttribute("breed", breed);
        return "breed_view";
    }

    @GetMapping("/index")
    public String indexpage() {
        return "redirect:/index.html";
    }

    @GetMapping("/member")
    public String memberpage(Model model) {
        List<Member> member_list = memberService.getList();
        model.addAttribute("member_list", member_list);
        return "member";
    }
    @GetMapping("/manager")
    public String managerpage() {
        return "manager";
    }

    @GetMapping("/Isponsor")
    public String isponsorpage() {
        return "Isponsor";
    }

    @GetMapping("/Lsponsor")
    public String lsponsorpage() {
        return "Lsponsor";
    }

    @GetMapping("/sponsor")
    public String sponsor() {
        return "sponsor";
    }

    @GetMapping("/community")
    public String communitypage(){
        return "community";
    }

    @ResponseBody
    @RequestMapping(value = {"/delete_faq"}, method = { RequestMethod.POST })
    public int delete_faq(@RequestBody List<Integer> faqIds) {
        return faqService.deleteFAQs(faqIds);
    }


    @RequestMapping(value = {"/faq_detail"} , method = { RequestMethod.GET })
    public String detail_faq(@RequestParam(value = "faqId", required=false) Integer faqId) {
        //System.out.println(faqId);
        //faqService.detailFAQs();
        //Optional<FAQ> faqOptional = faqRepository.findById(faqId);
        //System.out.println(faqOptional);
        return "faq_detail";
    }

    @ResponseBody
    @RequestMapping(value =  {"/delete_breed"}, method = {RequestMethod.POST})
    public  int delete_breed(@RequestBody List<Integer> breedIds){
        return breedService.deleteBreed(breedIds);
    }
    @RequestMapping(value = {"/breed_view"}, method = {RequestMethod.GET})
    public String view_breed(@RequestParam(value = "breedIds", required = false) Integer breedIds){
        return "breed_view";
    }
    @GetMapping("/breed_user")
    public String getBreedUserPage(Model model) {
        List<Breed> breedList = breedService.getAllBreeds()
                                            .stream()
                                            .filter(breed -> breed.getState() == null && "강아지".equals(breed.getType()))
                                            .collect(Collectors.toList()); //null값 허용, 수정 허용
        model.addAttribute("breed_list", breedList);
        return "breed_user";
    }
    @ResponseBody
    @GetMapping("/search")
    public List<Breed> searchBreeds(@RequestParam String query) {
        List<Breed> allBreeds = breedService.findAllBreeds()
                                                .stream()
                                                .filter(breed -> breed.getState() == null && "강아지".equals(breed.getType()))
                                                .collect(Collectors.toList());
        return allBreeds.stream()
                .filter(breed -> breed.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
    /*@ResponseBody
    @RequestMapping(value = {"/faq_create"}, method = { RequestMethod.GET })
    public Optional detail_faq(@RequestParam("faqId") Integer faqId) {
        //System.out.println(faqId);

        Optional<FAQ> faqOptional = faqRepository.findById(faqId);
        //System.out.println(faqOptional);
        return faqOptional;
    }*/
    /*@ResponseBody
    @RequestMapping(value = {"/faq_detail"}, method = { RequestMethod.GET })
    public int detail_faq(@RequestParam("faqId") Integer faqId, Optional<FAQ> faqOptional) {
        //System.out.println(faqId);
        // Integer faqId

        //System.out.println(faqOptional);
        //return faqOptional;
        return faqService.detailFAQs(faqId);
    }*/
}
