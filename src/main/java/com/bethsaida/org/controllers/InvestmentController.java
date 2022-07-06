package com.bethsaida.org.controllers;



import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.bethsaida.org.models.Branch;
import com.bethsaida.org.models.Customer;
import com.bethsaida.org.models.Disbursements;
import com.bethsaida.org.models.FileUploadUtil;
import com.bethsaida.org.models.Investment;
import com.bethsaida.org.models.InvestmentDTO;
import com.bethsaida.org.models.InvestmentExcelExporter;
import com.bethsaida.org.models.InvestmentStatus;
import com.bethsaida.org.models.User;
import com.bethsaida.org.repository.CustomerAccountRepo;
import com.bethsaida.org.repository.InvestmentRepository;
import com.bethsaida.org.service.InvestmentService;
import com.bethsaida.org.service.UserAccountService;

@CrossOrigin()
@RestController
public class InvestmentController {

//	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
    InvestmentService investmentService;
	
	@Autowired
	InvestmentRepository investmentRepository;
	
	@Autowired
	CustomerAccountRepo customerAccountRepo;
	
	@Autowired
    UserAccountService  userRepo;
	
    @GetMapping("/investment/findby")
    public List<Investment> findByAllParameters(@RequestParam(required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                @RequestParam(required = false) Integer accountNumber,
                                                @RequestParam(required = false) InvestmentStatus status,
                                                Pageable pageable
                                               ){
	return investmentService.findInvestmentByParameter(startDate, endDate, accountNumber, status, pageable);
	}
	
	
	@GetMapping("/investment/createdat")
	public ResponseEntity<List<Investment>> getInvestmentsByCreatedDate (@DateTimeFormat(pattern = "yyyy-MM-dd")@RequestParam LocalDate startDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate endDate){
	return new ResponseEntity<List<Investment>>(investmentService.findByDateBetween(startDate, endDate), HttpStatus.OK);
	}
	
	@GetMapping("/investment/searchByAccountNumber")
	public ResponseEntity<List<Investment>> findByAccountNumber(@RequestParam int accountNumber){
	return new ResponseEntity<List<Investment>>(investmentService.findByAccountNumber(accountNumber), HttpStatus.OK );
	}
	
	
	@GetMapping (value="list/Investment") 
	public List<Investment> getAllInvestment() { 
	return investmentService.getInvestment();
	}
    
	@GetMapping (value="pagedList/Investment") 
	public Page<Investment> getPagedInvestmentCount(Pageable pageable){
	return 	investmentService.getPagedInvestmentCount(pageable);
	}
	
    @GetMapping(value = "/investment/{id}")
    public Investment getInvestmentById(@PathVariable Long id){
    return investmentService.getInvestmentById(id);
    }
	 
	@DeleteMapping(value="/declineInvestment/{id}")
    public void deleteInvestment(@PathVariable Long id) {
    investmentService.deleteInvestment(id);
    }
   
    @PutMapping(value="/update/investment/{id}")
    public void updateUser (@RequestBody Investment investment, @PathVariable Long id){
 	investmentService.updateInvestment(id,investment);
    }
	 
    @PostMapping(value="/createInvestment",  consumes = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody Investment CreateInvestment (@RequestBody Investment investment, 
    		                                         @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, 
    		                                          @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate maturityDate) throws IOException, AddressException, MessagingException {
     Investment savedInvestment = investmentService.CreateInvestment(investment);
     return savedInvestment;
     }
    
    @PostMapping(value="/{id}/investmentUpload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	 public Investment uploadDocs(@ModelAttribute("investment") Investment investment, 
			                    @RequestParam(name="investFile", required = false) MultipartFile multipartInvestFile,
			                    @RequestParam(name="investFile1", required = false) MultipartFile multipartOtherImage,
	                            @PathVariable Long id) throws java.io.IOException, IOException {
	String documentName = StringUtils.cleanPath(multipartInvestFile.getOriginalFilename());
	String otherImageName = StringUtils.cleanPath(multipartOtherImage.getOriginalFilename());
	investment.setDocument(documentName);
	investment.setOtherImage(otherImageName);
	investmentRepository.save(investment);
	String uploadDir = "customer-photos/";	
    FileUploadUtil.saveFile(uploadDir, documentName, multipartInvestFile);
	FileUploadUtil.saveFile(uploadDir, otherImageName, multipartOtherImage);
	return investment;
	}
    
    @PostMapping(value="/firstLevelActivateInvestment/{id}")
    public Investment directorActivateInvestment(@PathVariable Long id, @RequestBody Investment investment) throws AddressException, MessagingException, IOException{
    return investmentService.firstLevelInvestmentActivation(id, investment);
    }
    
    //  @PreAuthorize("hasAuthority('MANAGING DIRECTOR')")
    @PostMapping(value="/activateInvestment/{id}")
    public Investment activateInvestment(@PathVariable Long id, @RequestBody Investment investment,  @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate maturityDate) throws Exception  
    {
    	Investment investmentExisting =  investmentService.getInvestmentById(id);
        if(investmentExisting  == null){
           throw new Exception("Investment Not Found");
         }
	 	return investmentService.activateInvestment(id, investment);
	}
    
    @PostMapping(value="/liquidateInvestment/{id}")
    public void liquidateInvestment(@PathVariable Long id, @RequestBody Investment investment) {
    investmentService.liquidateInvestment(id, investment);	
    }
    
	/*
	 * @PostMapping(value="/firstLevelApproveLiquidation/{id}") public void
	 * firstStageApproveLiquidation(@PathVariable Long id, @RequestBody Investment
	 * investment) { investmentService.firstLevelApproveLiquidation(id, investment);
	 * }
	 */
    
    @PostMapping(value="/sendLiquidationToAuditForReview/{id}")
    public void reviewLiquidation(@PathVariable Long id, @RequestBody Investment investment) throws AddressException, MessagingException, IOException {
    investmentService.investmentExecutiveToAuditLiquidationReview(id, investment);
    }
    
    @PostMapping(value="/auditToManagementLiquidationApproval/{id}")
    public void sendLiquidationRequestToManagementFromAudit(@PathVariable Long id, @RequestBody Investment investment) {
    investmentService.auditToManagementLiquidationApproval(id, investment);
    }
    
    @PostMapping(value="/approveLiquidation/{id}")
    public void approveLiquidation(@PathVariable Long id, @RequestBody Investment investment) {
    investmentService.approveLiquidation(id, investment);	
    }
    
    @PostMapping(value="/declineInvestment/{id}")
    public void declineInvestment(@PathVariable Long id, @RequestBody Investment investment) {
    investmentService.declineInvestment(id, investment);	
    }
    
    @PostMapping(value="/topUpInvestment/{id}")
    public Investment topUpInvestment(@PathVariable Long id, @RequestParam BigDecimal Amount, @RequestBody Investment investment) {
    return investmentService.topUpInvestment(id, Amount, investment);	
    }
    
    @PostMapping(value="/rollOverInvestment/{id}")
    public Investment rollOverInvestment(@PathVariable Long id, @RequestBody Investment investment) {
    return investmentService.rollOverInvestment(id, investment);
    }
    
    @GetMapping(value="/customerInvestmentDetails")
    public List<InvestmentDTO> getCustomerAndInvestmentInfo (Principal principal){
    Customer loggedInCustomer = customerAccountRepo.findByUserName(principal.getName());
    return investmentService.getCustomerAndInvestmentInfo(loggedInCustomer);
    }
    
    @GetMapping(value="/customerTotalPrincipal")
    public BigDecimal getCustomerTotalPrincipal(Principal principal) {
    Customer loggedInCustomer = customerAccountRepo.findByUserName(principal.getName());
    return investmentService.getCustomerTotalPrincipal(loggedInCustomer);
    }
    
    @GetMapping(value="/customerTotalMaturityInterest")
    public BigDecimal customerTotalMaturity(Principal principal) {
    Customer loggedInCustomer = customerAccountRepo.findByUserName(principal.getName());
    return investmentService.customerTotalMaturity(loggedInCustomer);
    }
    
    @GetMapping(value = "/dailyCustomerCurrentInterest")
    public BigDecimal customerDailyInterest(Principal principal) {
    Customer loggedInCustomer = customerAccountRepo.findByUserName(principal.getName());
    return investmentService.customerDailyInterest(loggedInCustomer);
    }
    
  //  @PreAuthorize("hasAuthority('MANAGING DIRECTOR')")
    @PostMapping(value="/approveDisbursement/{id}")
    public void approveDisbursement(@PathVariable Long id, @RequestBody Investment investment, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate maturityDate) throws Exception  
    {
    Investment investmentExisting =   investmentService.getInvestmentById(id);
    investmentService.approveDisbursement(id, investmentExisting);
	}
    
    @GetMapping(value="/centralTotalPrincipal")
    public BigDecimal getTotalPrincipal(){
    return investmentService.getTotalPrincipal();
    }
    
    @GetMapping(value="/centralTotalMaturityInterest")
    public BigDecimal getTotalMaturityInterest(){
    return investmentService.getTotalMaturityInterest();
    }
    
    @GetMapping(value="/centralMonthlyTotalMaturityInterest")
    public List<?> getTotalMonthlyMaturityInterest(){
    return investmentService.getTotalMonthlyMaturityInterest();
    }
    
    @GetMapping(value="/centralMonthlyPrincipal")
    public List<?> getTotalMonthlyPrincipal(){
    return investmentService.getTotalMonthlyPrincipal();
    }
    
    @GetMapping(value="/centralTotalActiveInvestments")
    public BigDecimal totalActiveInvestments() {
    return investmentService.totalActiveInvestments();
    }
    
    @GetMapping(value="/centralTotalWeeklyInvestmentMaturing")
    public BigDecimal totalWeeklyInvestmentMaturing() {
    return investmentService.totalWeeklyInvestmentMaturing();
    } 
    
    @GetMapping(value="/marketerTotalPrincipal")
    public BigDecimal getMarketerTotalPrincipal(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
    User loggedInUser = userRepo.findByUserName(authentication.getName());
    return investmentService.getTotalPrincipalByMarketer(loggedInUser);
    }
    
    @GetMapping(value="/marketerMonthlyInflow")
    public BigDecimal getMarketerMonthlyInflow(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
    User loggedInUser = userRepo.findByUserName(authentication.getName());
    return investmentService.getMarketerMonthlyInflow(loggedInUser);
    
    }
    
    @GetMapping(value="/marketers/investment")
    public Page<Investment> getListByMarketer(Pageable pageable){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
    User loggedInUser = userRepo.findByUserName(authentication.getName());
    return investmentService.findByMarketer(loggedInUser, pageable);
     }
    
    
    @GetMapping(value="branch/investments")
    public List<Investment> getListByBranch(Principal principal, Pageable pageable){
    User loggedInUser = userRepo.findByUserName(principal.getName());	
    Branch branchId = loggedInUser.getBranch();
    return investmentService.findByUserBranch(branchId,pageable );
    }
    
    @GetMapping(value="/branchTotalPrincipal")
    public BigDecimal getBranchPrincipal(Principal principal) {
    User loggedInUser = userRepo.findByUserName(principal.getName());
    Branch branchId = loggedInUser.getBranch();
    return investmentService.getTotalBranchPrincipal(branchId);
    }
    
    @GetMapping(value="/branchTotalInterest")
    public BigDecimal getBranchInterest(Principal principal) {
    User loggedInUser = userRepo.findByUserName(principal.getName());
    Branch branchId = loggedInUser.getBranch();
    return investmentService.getTotalBranchInterest(branchId);
    }
    
    @GetMapping(value="/branchMonthlyReporting/{id}")
    public List<?> getBranchMonthlyReport(Branch branch) {
    return investmentService.getTotalMonthlyBranchData(branch);
    }
    
    @GetMapping("/investment/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
    response.setContentType("application/octet-stream");
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String currentDateTime = dateFormatter.format(new Date(0));
    String headerKey = "Content-Disposition";
    String headerValue = "attachment; filename=investments_" + currentDateTime + ".xlsx";
    response.setHeader(headerKey, headerValue);
    List<Investment> listinvestment = investmentService.getInvestment();
    InvestmentExcelExporter excelExporter = new InvestmentExcelExporter(listinvestment);
    excelExporter.export(response);    
    }  
    
    // All Reporting EndPoints Start here 
    
    @GetMapping(value= "/activeInvestments")
    public List<Investment> ActiveInvestments(){
    return investmentService.getAllActiveInvestments();
    }
    
    @GetMapping(value= "/disbursedInvestments")
    public List<Investment> DisbursedInvestments(){
    return investmentService.getAllDisbursedInvestments();
    }
    
    @GetMapping(value= "/maturedInvestments")
    public List<Investment> MaturedInvestments(){
    return investmentService.getAllMaturedInvestments();
    }
    
    @GetMapping(value= "/totalMaturedInvestments")
    public Long totalMaturedInvestments() {
    return investmentService.getTotalMaturedInvestments();
    }
    
    @GetMapping(value="/rolledOverInvestments")
	public List<Investment> rolledOverInvestments(){
	return investmentService.getAllRolledOverInvestments();
	}
	
    @GetMapping(value="/totalCountRolledOverInvestment")
    public Long getTotalRolledOverInvestments() {
    return investmentService.getTotalRolledOverInvestments();
    }
	
    @GetMapping(value="/toppedUpInvestmentsList")
    public List<Investment> getAllToppedUpInvestments(){
    return investmentService.getAllToppedUpInvestments();
    }
	
    @GetMapping(value="/totalCountToppedUpInvestment")
    public Long getTotalToppedUpInvestments()  {
    return investmentService.getTotalToppedUpInvestments();
    }
	
	@GetMapping(value="/upfrontInterestsPaidInvesmentsList")
    public List<Investment> getAllInterestPaidInvestments(){
    return investmentService.getAllInterestPaidInvestments();
    }
	
	@GetMapping(value="/totalUpfrontInterestsPaidInvesments")
	public Long getTotalUpFrontMaturityPaidInvestments() {
	return investmentService.getTotalUpFrontMaturityPaidInvestments();
	}
	
 }
