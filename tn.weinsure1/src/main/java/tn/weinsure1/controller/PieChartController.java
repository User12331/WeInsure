package tn.weinsure1.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.annotation.MultipartConfig;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;

import org.chartistjsf.model.chart.AspectRatio;
import org.chartistjsf.model.chart.ChartSeries;
import org.chartistjsf.model.chart.PieChartModel;
import org.primefaces.event.ItemSelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import tn.weinsure1.entities.sinister;
import tn.weinsure1.service.IsinisterService;








@Scope(value = "session")
@Controller(value = "PieController") // Name of the bean in Spring IoC
@ELBeanName(value = "PieController") // Name of the bean used by JSF
@Join(path = "/", to = "/temm.jsf")
@MultipartConfig
public class PieChartController implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 @Autowired
	    IsinisterService sinS;
	 
	 private List<sinister> sins;
	
	private PieChartModel pieChartModel;
    public PieChartController() {
  
    }
    @PostConstruct
    public void init() {
        pieChartModel = new PieChartModel();
        
        pieChartModel.addLabel("Bananas");
		pieChartModel.addLabel("Apples");
		pieChartModel.addLabel("Grapes");

		pieChartModel.set(20);
		pieChartModel.set(10);
		pieChartModel.set(30);

		pieChartModel.setShowTooltip(true);
          
       
    }
    public void pieItemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected", "Item Value: "
                + pieChartModel.getData().get(event.getItemIndex()));
        FacesContext.getCurrentInstance().addMessage(event.getComponent().getClientId(), msg);
    }
    public PieChartModel getPieChartModel() {
        return pieChartModel;
    }
    public void setPieChartModel(PieChartModel pieChartModel) {
        this.pieChartModel = pieChartModel;
    }
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    
    
    
}