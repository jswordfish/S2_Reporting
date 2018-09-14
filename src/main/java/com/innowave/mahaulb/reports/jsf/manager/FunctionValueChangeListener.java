package com.innowave.mahaulb.reports.jsf.manager;

import javax.faces.application.Application;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

import org.primefaces.context.RequestContext;

import com.innowave.mahaulb.reports.data.FromFieldFunction;
import com.innowave.mahaulb.reports.data.Function;

public class FunctionValueChangeListener implements ValueChangeListener{

	@Override
	public void processValueChange(ValueChangeEvent e) throws AbortProcessingException {
		// TODO Auto-generated method stub
//		FromFieldFunction field = (FromFieldFunction) FacesContext.getCurrentInstance().
//				getExternalContext().getSessionMap().get("field");
//		field.setFunction(Function.valueOf(event.getNewValue().toString()));
//		RequestContext.getCurrentInstance().update("reportQueryForm:fromQuery");
		 UIData data = (UIData) e.getComponent().findComponent("reportQueryForm:fromQuery");
		    int rowIndex = data.getRowIndex();
		    Object myNewValue = e.getNewValue();
		    Object myOldValue = e.getOldValue();
		    System.out.println(myNewValue);
		    FacesContext context = FacesContext.getCurrentInstance();
		    Application application = context.getApplication();
		    ReportManagerNew managerNew = application.evaluateExpressionGet(context, "#{reportManagerNew}", ReportManagerNew.class);
		    FromFieldFunction fieldFunction = managerNew.getQueryBuilder().getFromClause().getFieldFunctions().get(rowIndex);
		    fieldFunction.setFunction(Function.valueOf(myOldValue.toString()));
		    RequestContext.getCurrentInstance().update("reportQueryForm:fromQuery");
		    
	}

}
