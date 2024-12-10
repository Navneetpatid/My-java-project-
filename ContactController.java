package com.janaushadhi.adminservice.controller;

import com.janaushadhi.adminservice.requestpayload.ContactRequest;
import com.janaushadhi.adminservice.requestpayload.ContactsDeletePayload;
import com.janaushadhi.adminservice.responsepayload.GetAllContact;
import com.janaushadhi.adminservice.responsepayload.GetAllDeleteContact;
import com.janaushadhi.adminservice.serviceimpl.ContactServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/contact")
public class ContactController {

@Autowired
private ContactServiceImpl contactService;

    @PostMapping(value = "/addContact")
    public ResponseEntity< Object> addContact(@RequestBody ContactRequest contactRequest) throws IOException {
        return  ResponseEntity.ok(contactService.addContact(contactRequest)) ;
    }

    @GetMapping(value = "/contactStatusUpdate")
    public ResponseEntity<Map<String, Object>> contactStatusUpdate(@RequestParam Long id,@RequestParam short status){
        return ResponseEntity.ok(contactService.contactStatusUpdate( id,status));
    }
    @GetMapping(value = "/getByContactId")
    public Map<String, Object> getByContactId( @RequestParam Long id) {
        return contactService.getByContactId(id);
    }





    @PostMapping( value = "/getAllContact")
    public Map<String,Object> getAllContact(@RequestBody GetAllContact getAllContact){
        return contactService.getAllContact(getAllContact);
    }

    @PostMapping( value = "/getAllContactByDepartment")
    public Map<String,Object> getAllContactByDepartment(@RequestBody GetAllContact getAllContact){
        return contactService.getAllContactByDepartment(getAllContact);
    }

    @DeleteMapping(value = "/softDeleteContacts")
    public Map<Object, Object> deleteContactBulk(@RequestBody ContactsDeletePayload ContactIds) {
        return contactService.deleteContactBulk(ContactIds);

    }


    @PostMapping( value = "/getAllDeleteContact")
    public Map<String,Object> getAllDeleteContact(@RequestBody GetAllDeleteContact getAllContact){
        return contactService.getAllDeleteContact(getAllContact);
    }

}
