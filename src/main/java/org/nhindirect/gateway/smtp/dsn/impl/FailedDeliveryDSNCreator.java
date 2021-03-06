/* 
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Authors:
   Greg Meyer      gm2552@cerner.com
 
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
in the documentation and/or other materials provided with the distribution.  Neither the name of the The NHIN Direct Project (nhindirect.org). 
nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.nhindirect.gateway.smtp.dsn.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.mailet.Mailet;
import org.nhindirect.common.mail.dsn.DSNFailureTextBodyPartGenerator;
import org.nhindirect.common.mail.dsn.DSNGenerator;
import org.nhindirect.common.mail.dsn.DSNStandard.DSNStatus;
import org.nhindirect.common.mail.dsn.impl.DefaultDSNFailureTextBodyPartGenerator;
import org.nhindirect.common.mail.dsn.impl.HumanReadableTextAssemblerFactory;
import org.nhindirect.common.options.OptionsManager;
import org.nhindirect.gateway.GatewayConfiguration;

public class FailedDeliveryDSNCreator extends AbstractDSNCreator 
{
	static
	{		
		initJVMParams();
	}
	
	private synchronized static void initJVMParams()
	{
		/*
		 * Configuration parameters
		 */
		final Map<String, String> JVM_PARAMS = new HashMap<String, String>();
		JVM_PARAMS.put(FailedDeliveryDSNCreatorOptions.DSN_FAILED_PREFIX, "org.nhindirect.gateway.smtp.dsn.impl.DeliveryFailureDSNFailedPrefix");
		JVM_PARAMS.put(FailedDeliveryDSNCreatorOptions.DSN_MTA_NAME, "org.nhindirect.gateway.smtp.dsn.imp.DeliveryFailureDSNMTAName");
		JVM_PARAMS.put(FailedDeliveryDSNCreatorOptions.DSN_POSTMASTER, "org.nhindirect.gateway.smtp.dsn.impl.DeliveryFailureDNSPostmaster");
		JVM_PARAMS.put(FailedDeliveryDSNCreatorOptions.DSN_FAILED_RECIP_TITLE, "org.nhindirect.gateway.smtp.dsn.impl.DeliveryFailureDSNFaileRecipTitle");
		JVM_PARAMS.put(FailedDeliveryDSNCreatorOptions.DSN_FAILED_ERROR_MESSAGE, "org.nhindirect.gateway.smtp.dsn.impl.DeliveryFailureDSNFailedErrorMessage");
		JVM_PARAMS.put(FailedDeliveryDSNCreatorOptions.DSN_FAILED_HEADER, "org.nhindirect.gateway.smtp.dsn.impl.DeliveryFailureDSNFailedHeader");
		JVM_PARAMS.put(FailedDeliveryDSNCreatorOptions.DSN_FAILED_FOOTER, "org.nhindirect.gateway.smtp.dsn.impl.DeliveryFailureDSNFailedFooter");
		
		OptionsManager.addInitParameters(JVM_PARAMS);
	}
	
	///CLOVER:OFF
	public FailedDeliveryDSNCreator(DSNGenerator generator, String postmasterMailbox, String reportingMta, 
			DSNFailureTextBodyPartGenerator textGenerator)
	{
		this.mailet = null;
		this.generator = generator;
		this.postmasterMailbox = postmasterMailbox;
		this.reportingMta = reportingMta;
		this.textGenerator = textGenerator;
		this.dsnStatus = DSNStatus.DELIVERY_OTHER;
	}
	///CLOVER:ON
	
	public FailedDeliveryDSNCreator(Mailet mailet)
	{
		this.mailet = mailet;
		
		this.dsnStatus = DSNStatus.DELIVERY_OTHER;
		
		generator = new DSNGenerator(GatewayConfiguration.getConfigurationParam(FailedDeliveryDSNCreatorOptions.DSN_FAILED_PREFIX, 
				mailet, null, FailedDeliveryDSNCreatorOptions.DEFAULT_PREFIX));
		
		postmasterMailbox = GatewayConfiguration.getConfigurationParam(FailedDeliveryDSNCreatorOptions.DSN_POSTMASTER, 
				mailet, null, FailedDeliveryDSNCreatorOptions.DEFAULT_POSTMASTER);
		
		reportingMta = GatewayConfiguration.getConfigurationParam(FailedDeliveryDSNCreatorOptions.DSN_MTA_NAME, 
				mailet, null, FailedDeliveryDSNCreatorOptions.DEFAULT_MTA_NAME);
		
		
		textGenerator = new DefaultDSNFailureTextBodyPartGenerator(
				GatewayConfiguration.getConfigurationParam(FailedDeliveryDSNCreatorOptions.DSN_FAILED_HEADER, 
						mailet, null, FailedDeliveryDSNCreatorOptions.DEFAULT_HEADER), 
						GatewayConfiguration.getConfigurationParam(FailedDeliveryDSNCreatorOptions.DSN_FAILED_FOOTER, 
						mailet, null, FailedDeliveryDSNCreatorOptions.DEFAULT_FOOTER), 
						GatewayConfiguration.getConfigurationParam(FailedDeliveryDSNCreatorOptions.DSN_FAILED_RECIP_TITLE, 
						mailet, null, FailedDeliveryDSNCreatorOptions.DEFAULT_FAILED_RECIP_TITLE), 
						FailedDeliveryDSNCreatorOptions.DEFAULT_ERROR_MESSAGE_TITLE,
					GatewayConfiguration.getConfigurationParam(FailedDeliveryDSNCreatorOptions.DSN_FAILED_ERROR_MESSAGE, 
								mailet, null, FailedDeliveryDSNCreatorOptions.DEFAULT_ERROR_MESSAGE),
			    HumanReadableTextAssemblerFactory.getInstance());
	}
}
