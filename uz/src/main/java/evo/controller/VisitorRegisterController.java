package evo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import evo.dao.SysUserMapper;
import evo.model.SysUser;
import evo.model.UserMonthTime;
import evo.model.VisitorRegister;
import evo.service.IExportExcelService;
import evo.service.IVisitorRegisterService;
import evo.util.DateUtil;
import evo.util.Page;

@Controller
@RequestMapping("/visitorRegisterController")
public class VisitorRegisterController {
	
	@Autowired
	private IVisitorRegisterService visitorRegisterService;
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	
	private IExportExcelService exportExcelService;
	/**
	 * 
	 * @Title: findVisitorRegisterList
	 * @Description: 查询所有访问者记录
	 * @author Demo demo_@evo_com
	 * @param @return    设定文件
	 * @return ModelAndView    返回类型
	 * @throws
	 */
	@RequestMapping("/findVisitorRegisterList")
	public ModelAndView findVisitorRegisterList(){
		ModelAndView view = new ModelAndView();
		
		List<VisitorRegister> list = visitorRegisterService.findVisitorRegisterList();
		view.addObject("visitorRegister", list);
		view.setViewName("visit");
		return view;
		
	}	
	
	/**
	 * 
	 * @Title: findVisitorRegisterByVisitorList
	 * @Description: 根据访问者的手机号查询访问者记录List
	 * @author Demo demo_@evo_com
	 * @param @param phoneNum
	 * @param @return    设定文件
	 * @return ModelAndView    返回类型
	 * @throws
	 */
	@RequestMapping("/findVisitorRegisterByVisitorList")
	public ModelAndView findVisitorRegisterByVisitorList(String phoneNum){
		ModelAndView view = new ModelAndView();
		
		List<VisitorRegister> list = visitorRegisterService.findVisitorRegisterByVisitorList(phoneNum);
		view.addObject("visitorRegisterByPhoneNum", list);
		view.setViewName("visit");
		return view;
		
	}
	
	/**
	 * 
	 * @Title: insertVisitorRegister
	 * @Description: 插入访问记录
	 * @author Demo demo_@evo_com
	 * @param @param visitorRegister    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	@RequestMapping("/insertVisitorRegister")
	public String insertVisitorRegister(VisitorRegister visitorRegister){
		visitorRegisterService.insertVisitorRegister(visitorRegister);
		return "redirect:findVisitorListByPage/0.do";
	}
	
	/**
	 * @throws Exception 
	 * 
	 * @Title: deleteVisitorRegisterByVisitorRegisterId
	 * @Description: 根据VisitorRegisterId删除访问记录
	 * @author Demo demo_@evo_com
	 * @param @param visitorRegisterId    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	@RequestMapping("/deleteVisitorRegisterByVisitorRegisterId/{visitorRegisterId}")
	public void deleteVisitorRegisterByVisitorRegisterId(@PathVariable Integer visitorRegisterId,HttpServletResponse response) throws Exception{
		visitorRegisterService.deleteVisitorRegisterByVisitorRegisterId(visitorRegisterId);
		response.getWriter().print("{msg:'t'}");
	}
	/**
	 * @param visitorRegister
	 * @param currentPage
	 * @return
	 */
	@RequestMapping("/findVisitorListByPage/{currentPage}")
	public ModelAndView findVisitorListByPage(VisitorRegister visitorRegister,@PathVariable int currentPage){
		ModelAndView view = new ModelAndView();
		Page page = new Page();
		page.setCurrentPage(currentPage);
		List<VisitorRegister> list = visitorRegisterService.findVisitorRegisterListByPage(visitorRegister, page);
		for(VisitorRegister vr : list){
			String registerTime = vr.getRegisterTime();
			String substring = registerTime.substring(0, registerTime.lastIndexOf("."));
			vr.setRegisterTime(substring);
		}
		view.addObject("visitorListByPage", list);
		view.addObject("page", page);
		view.setViewName("visit");
		return view;
	}
	
	@RequestMapping("/findCurDayUserRecord")
	public void findCurDayUserRecord(HttpServletResponse response) throws Exception{
		List<VisitorRegister> listWork =	visitorRegisterService.findVisitorRegisterList();
		
		for(VisitorRegister register:listWork){
			if(StringUtils.isNotEmpty(register.getRegisterTime())){
				String tmp =register.getRegisterTime() ;
				register.setRegisterTime(tmp.substring(0,tmp.lastIndexOf(".")));
			}
		}
		//JsonConfig jsonConfig = new JsonConfig();  
		//jsonConfig.setIgnoreDefaultExcludes(false);
		//jsonConfig.setExcludes(new String[]{"visitors", "channelPersonals"}); 
		
		
		Map map =new HashMap<String,VisitorRegister>();
		map.put("userRecord", listWork);
		JSONObject  onObject = JSONObject.fromObject(map);
		 response.getWriter().print(onObject.toString());
	}
	

	
	public static void main(String[] args) throws Exception {
		String str = "2015-09-16 12:44:16";
		//"1442335456000";
		//1442826753000
		long time = DateUtil.getLongTime(str);
		
		System.out.println(time);

		Date date = new Date(1442851200000L);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		System.out.println(dateString);
	}
	
	
	

}
