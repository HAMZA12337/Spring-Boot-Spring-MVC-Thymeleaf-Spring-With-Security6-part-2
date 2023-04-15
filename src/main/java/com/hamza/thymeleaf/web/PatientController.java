package com.hamza.thymeleaf.web;


import com.hamza.thymeleaf.entities.Patient;
import com.hamza.thymeleaf.repositories.PatientRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {

 private PatientRepository patientRepository;

 @GetMapping(path = "/user/index")
  public String patients (Model model,
                          @RequestParam(name="page",defaultValue="0") int page,
                          @RequestParam(name="size",defaultValue = "5") int size ,
                          @RequestParam(name="keyword",defaultValue ="") String keyword){
  Page<Patient> patients=patientRepository.findByNomContains(keyword,PageRequest.of(page,size));
  model.addAttribute("listPatients",patients.getContent());
  model.addAttribute("pages",new int[patients.getTotalPages()]);
  model.addAttribute("currentPage",page);
  model.addAttribute("keyword",keyword);

  return "patients";}



 @GetMapping("/admin/delete")
 public String delete (Long id,String keyword,int page){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
 }

 @GetMapping("/")
 public String home (){

  return "redirect:/user/index";
 }


 @GetMapping("/patients")
 @ResponseBody
public List<Patient> listPatient(){

return patientRepository.findAll();}

@GetMapping("/admin/formPatients")
 public String formPatient(Model model){
model.addAttribute("patient",new Patient());
 return "formPatients";};


@PostMapping("/admin/ save")
 public String save(Model model , @Valid Patient patient, BindingResult bindingResult,@RequestParam(defaultValue ="0") int page,
                     @RequestParam(defaultValue ="") String keyword){

  if(bindingResult.hasErrors()) return "formPatients";
   patientRepository.save(patient);
 return "redirect:/index?page="+page+"&keyword="+keyword;}

@GetMapping("/admin/editPatient")
public String editPatient(Model model,Long id,String keyword,int page) throws Exception {
Patient patient=patientRepository.findById(id).orElse(null);

if(patient==null) throw new Exception("Patient introuvable");
model.addAttribute("patient",patient);
model.addAttribute("keyword",keyword);
 model.addAttribute("page",page);
return "editPatient";}

 @GetMapping("/logout")
 public String logout (){

  return "redirect:/logout";
 }




}
