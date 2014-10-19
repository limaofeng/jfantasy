package com.fantasy.framework.ws.axis2;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.modules.Module;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;

public class CustomModule implements Module {
	public void applyPolicy(Policy policy, AxisDescription axisDescription) throws AxisFault {
		System.out.println("###############applyPolicy##############");
	}

	public boolean canSupportAssertion(Assertion assertion) {
		System.out.println("##############canSupportAssertion##############");
		return true;
	}

	public void engageNotify(AxisDescription axisDescription) throws AxisFault {
		System.out.println("##############engageNotify#############");
	}

	public void init(ConfigurationContext ctx, AxisModule module) throws AxisFault {
		System.out.println("##############init##############");
	}

	public void shutdown(ConfigurationContext ctx) throws AxisFault {
		System.out.println("##############shutdown#############");
	}
}
