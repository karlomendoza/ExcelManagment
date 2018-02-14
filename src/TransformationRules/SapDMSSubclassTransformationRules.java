package TransformationRules;

public class SapDMSSubclassTransformationRules {

	public static String subClassTransformation(String documentNumber, String documentType) {
		if (documentNumber.startsWith("CR-LOCR"))
			return "Equipment, Facility, and Utility (EFU) - LO - Lock Out - Tag Out";
		else if (documentNumber.startsWith("CR-OPCR"))
			return "Equipment, Facility, and Utility (EFU) - OP - Operational Parameters";
		else if (documentNumber.startsWith("CR-OSCR"))
			return "Equipment, Facility, and Utility (EFU) - OS - Operational Specification";
		else if (documentNumber.startsWith("CR-UTCR"))
			return "Equipment, Facility, and Utility (EFU) - UT - Utilization Table";

		else if (documentNumber.startsWith("971-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("973-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("974-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("976-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("977-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("978-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("980-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("985-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("982-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("983-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("995-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("961-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("964-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("898-"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("CR-ENV"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("CR-HKG"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("CR-INF"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("CR-MAI"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("CR-MAT"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("CR-MIS"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("CR-SAP"))
			return "Non-Quality System Document";
		else if (documentNumber.startsWith("CR-PRO"))
			return "Non-Quality System Document";

		else if (documentNumber.startsWith("502-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("505-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("509-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("513-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("514-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("517-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("518-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("519-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("520-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("522-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("559-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("560-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("597-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("599-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("850-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("266-"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("CR-BOPCAS"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("CR-BOPCPR"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("CR-BOPINJ"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("CR-BOPLIM"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("CR-BOPMCH"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("CR-BOPMQA"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("CR-BOPPKG"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("CR-BOPSLP"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("CR-BOPSPC"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("CR-BOPWHG"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("CR-BOPWSH"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("CR-BOPL"))
			return "Production Method - Manufacturing Work Instructions";
		else if (documentNumber.startsWith("CR-EXT"))
			return "Production Method - Manufacturing Work Instructions";

		else if (documentNumber.startsWith("92.D"))
			return "Production Method - Process Specification - Drug";
		else if (documentNumber.startsWith("92.R"))
			return "Production Method - Process Specification - Rubber";
		else if (documentNumber.startsWith("92.S"))
			return "Production Method - Process Specification - Sterilization";

		else if (documentNumber.startsWith("92.T"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("Costa Rica -"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("256-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("249-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("219-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("561-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("562-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("563-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("564-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("566-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("568-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("569-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("571-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("576-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("579-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("580-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("581-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("583-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("595-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("596-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("930-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("932-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("100-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("101-"))
			return "Production Method - Process Specification - Technical";
		else if (documentNumber.startsWith("105-"))
			return "Production Method - Process Specification - Technical";

		else if (documentNumber.startsWith("IS.PP.507.202.LA"))
			return "Production Method - Production Line Setup Instructions";
		else if (documentNumber.startsWith("IS.PP.507.203.LA"))
			return "Production Method - Production Line Setup Instructions";
		else if (documentNumber.startsWith("IS.PP.506.201"))
			return "Production Method - Production Line Setup Instructions";
		else if (documentNumber.startsWith("964-97700-001-CR"))
			return "Production Method - Production Line Setup Instructions";

		else if (documentNumber.startsWith("Q"))
			return "Product Packaging, Labeling, and Manuals - Artwork";
		else if (documentNumber.startsWith("D"))
			return "Product Packaging, Labeling, and Manuals - Artwork";
		else if (documentNumber.startsWith("429-"))
			return "Product Packaging, Labeling, and Manuals - Artwork";
		else if (documentNumber.startsWith("430-"))
			return "Product Packaging, Labeling, and Manuals - Artwork";
		else if (documentNumber.startsWith("425-"))
			return "Product Packaging, Labeling, and Manuals - Artwork";
		else if (documentNumber.startsWith("735-"))
			return "Product Packaging, Labeling, and Manuals - Artwork";
		else if (documentNumber.startsWith("736-"))
			return "Product Packaging, Labeling, and Manuals - Artwork";

		else if (documentNumber.startsWith("DG)"))
			return "Product Packaging, Labeling, and Manuals - Drawing";
		else if (documentNumber.startsWith("DP"))
			return "Product Packaging, Labeling, and Manuals - Drawing";

		else if (documentNumber.startsWith("441-"))
			return "Product Packaging, Labeling, and Manuals - Other";
		else if (documentNumber.startsWith("444-"))
			return "Product Packaging, Labeling, and Manuals - Other";
		else if (documentNumber.startsWith("445-"))
			return "Product Packaging, Labeling, and Manuals - Other";

		else if (documentNumber.startsWith("40"))
			return "Product Packaging, Labeling, and Manuals - Printed Material Summary";
		else if (documentNumber.startsWith("50"))
			return "Product Packaging, Labeling, and Manuals - Printed Material Summary";
		else if (documentNumber.startsWith("55"))
			return "Product Packaging, Labeling, and Manuals - Printed Material Summary";
		else if (documentNumber.startsWith("845-"))
			return "Product Packaging, Labeling, and Manuals - Printed Material Summary";

		else if (documentNumber.startsWith("10.70"))
			return "Product Packaging, Labeling, and Manuals - Specification";
		else if (documentNumber.startsWith("10.78"))
			return "Product Packaging, Labeling, and Manuals - Specification";
		else if (documentNumber.startsWith("13.70"))
			return "Product Packaging, Labeling, and Manuals - Specification";
		else if (documentNumber.startsWith("13.78"))
			return "Product Packaging, Labeling, and Manuals - Specification";
		else if (documentNumber.startsWith("20."))
			return "Product Packaging, Labeling, and Manuals - Specification";
		else if (documentNumber.startsWith("70."))
			return "Product Packaging, Labeling, and Manuals - Specification";

		else if (documentNumber.startsWith("T") && documentType.equals("Z09"))
			return "Product Packaging, Labeling, and Manuals - Template/Print on Demand";
		else if (documentNumber.startsWith("599-"))
			return "Product Packaging, Labeling, and Manuals - Template/Print on Demand";

		else if ((documentType.equals("Z10") || documentType.equals("Z11") || documentType.equals("Z05"))
				&& (!documentNumber.startsWith("IS.PP.507.202.LA") && !documentNumber.startsWith("IS.PP.507.203.LA")
						&& !documentNumber.startsWith("IS.PP.506.201")))
			return "Quality System Procedure";
		else if (documentNumber.startsWith("970-"))
			return "Quality System Procedure";
		else if (documentNumber.startsWith("972-"))
			return "Quality System Procedure";
		else if (documentNumber.startsWith("975-"))
			return "Quality System Procedure";
		else if (documentNumber.startsWith("978-"))
			return "Quality System Procedure";
		else if (documentNumber.startsWith("CR-DDC"))
			return "Quality System Procedure";
		else if (documentNumber.startsWith("CR-DOC"))
			return "Quality System Procedure";
		else if (documentNumber.startsWith("CR-IQA"))
			return "Quality System Procedure";
		else if (documentNumber.startsWith("CR-MET"))
			return "Quality System Procedure";
		else if (documentNumber.startsWith("CR-QAE"))
			return "Quality System Procedure";
		else if (documentNumber.startsWith("CR-TRG"))
			return "Quality System Procedure";

		else if (documentNumber.startsWith("51."))
			return "Quality System Record - Other";

		else if (documentNumber.startsWith("15."))
			return "Sampling Plan";

		else if (documentNumber.startsWith("950-"))
			return "Servicing";
		else if (documentNumber.startsWith("965-"))
			return "Servicing";
		else if (documentNumber.startsWith("966-"))
			return "Servicing";
		else if (documentNumber.startsWith("969-"))
			return "Servicing";
		else if (documentNumber.startsWith("982-"))
			return "Servicing";
		else if (documentNumber.startsWith("984-"))
			return "Servicing";
		else if (documentNumber.startsWith("987-"))
			return "Servicing";
		else if (documentNumber.startsWith("990-"))
			return "Servicing";
		else if (documentNumber.startsWith("845-"))
			return "Servicing";
		else if (documentNumber.startsWith("847-"))
			return "Servicing";

		else if (documentNumber.startsWith("846-"))
			return "Servicing - Product";
		else if (documentNumber.startsWith("894-"))
			return "Servicing - Product";
		else if (documentNumber.startsWith("895-"))
			return "Servicing - Product";
		else if (documentNumber.startsWith("896-"))
			return "Servicing - Product";

		else if (documentNumber.startsWith("11."))
			return "Spec - API and Excipient";
		else if (documentNumber.startsWith("14."))
			return "Spec - API and Excipient";

		else if (documentNumber.startsWith("32"))
			return "Spec - Commodity and Process Summary";
		else if (documentNumber.startsWith("34"))
			return "Spec - Commodity and Process Summary";
		else if (documentNumber.startsWith("35"))
			return "Spec - Commodity and Process Summary";
		else if (documentNumber.startsWith("36"))
			return "Spec - Commodity and Process Summary";
		else if (documentNumber.startsWith("CP."))
			return "Spec - Commodity and Process Summary";
		else if (documentNumber.endsWith(".BOM"))
			return "Spec - Commodity and Process Summary";
		else if (documentType.startsWith("Z01"))
			return "Spec - Commodity and Process Summary";

		else if (documentNumber.startsWith("980-"))
			return "Spec - Environmental Specification";
		else if (documentNumber.startsWith("904-"))
			return "Spec - Environmental Specification";
		else if (documentNumber.startsWith("910-"))
			return "Spec - Environmental Specification";

		else if (documentType.startsWith("Z13"))
			return "Spec - Marketed Product Stability Protocol";
		else if (documentNumber.startsWith("45."))
			return "Spec - Marketed Product Stability Protocol";

		else if (documentNumber.startsWith("95."))
			return "Spec - Performance Specification";

		else if (documentNumber.startsWith("249-"))
			return "Spec - Printed Circuit Board - Assembly/Schematic";
		else if (documentNumber.startsWith("807-"))
			return "Spec - Printed Circuit Board - Assembly/Schematic";
		else if (documentNumber.startsWith("805-"))
			return "Spec - Printed Circuit Board - Assembly/Schematic";
		else if (documentNumber.startsWith("810-"))
			return "Spec - Printed Circuit Board - Assembly/Schematic";
		else if (documentNumber.startsWith("261-"))
			return "Spec - Printed Circuit Board - Assembly/Schematic";
		else if (documentNumber.startsWith("263-"))
			return "Spec - Printed Circuit Board - Assembly/Schematic";
		else if (documentNumber.startsWith("264-"))
			return "Spec - Printed Circuit Board - Assembly/Schematic";
		else if (documentNumber.startsWith("265-"))
			return "Spec - Printed Circuit Board - Assembly/Schematic";

		else if (documentNumber.startsWith("80."))
			return "Spec - Product Purchase Specification";
		else if (documentNumber.startsWith("85."))
			return "Spec - Product Purchase Specification";

		else if (documentNumber.startsWith("234-"))
			return "Spec - Software Specification";
		else if (documentNumber.startsWith("238-"))
			return "Spec - Software Specification";
		else if (documentNumber.startsWith("273-"))
			return "Spec - Software Specification";
		else if (documentNumber.startsWith("278-"))
			return "Spec - Software Specification";
		else if (documentNumber.startsWith("259-"))
			return "Spec - Software Specification";

		else if (documentNumber.startsWith("60."))
			return "Spec - Sterile Solution";

		else if (documentNumber.startsWith("TDS."))
			return "Spec - Technical Data Sheet";
		else if (documentType.startsWith("Z02"))
			return "Spec - Technical Data Sheet";

		else if (documentNumber.startsWith("61."))
			return "Spec - Test and Inspection Specification";
		else if (documentNumber.startsWith("62."))
			return "Spec - Test and Inspection Specification";
		else if (documentNumber.startsWith("63."))
			return "Spec - Test and Inspection Specification";
		else if (documentNumber.startsWith("64."))
			return "Spec - Test and Inspection Specification";
		else if (documentNumber.startsWith("68."))
			return "Spec - Test and Inspection Specification";
		else if (documentNumber.startsWith("69."))
			return "Spec - Test and Inspection Specification";
		else if (documentNumber.startsWith("526-"))
			return "Spec - Test and Inspection Specification";
		else if (documentNumber.startsWith("541-"))
			return "Spec - Test and Inspection Specification";
		else if (documentNumber.startsWith("551-"))
			return "Spec - Test and Inspection Specification";
		else if (documentNumber.startsWith("588-"))
			return "Spec - Test and Inspection Specification";

		else if (documentNumber.startsWith("90.B"))
			return "Test Method - Biologic";
		else if (documentNumber.startsWith("94.B"))
			return "Test Method - Biologic";
		else if (documentNumber.startsWith("90.STB"))
			return "Test Method - Biologic";
		else if (documentNumber.startsWith("TM-BIO-HCR- XXXX"))
			return "Test Method - Biologic";

		else if (documentNumber.startsWith("90.C"))
			return "Test Method - Chemical";
		else if (documentNumber.startsWith("94.C"))
			return "Test Method - Chemical";
		else if (documentNumber.startsWith("90.STC"))
			return "Test Method - Biologic";
		else if (documentNumber.startsWith("TM-CHE-HCR- XXXX"))
			return "Test Method - Chemical";

		else if (documentNumber.startsWith("TM-DIM-HCR- XXXX"))
			return "Test Method - Functional";
		else if (documentNumber.startsWith("TM-VIS-HCR- XXXX"))
			return "Test Method - Functional";

		else if (documentNumber.startsWith("90.I"))
			return "Test Method - Identity";
		else if (documentNumber.startsWith("94.I"))
			return "Test Method - Identity";
		else if (documentNumber.startsWith("90.STI"))
			return "Test Method - Identity";
		else if (documentNumber.startsWith("STM.90.I"))
			return "Test Method - Identity";

		else if (documentNumber.startsWith("90.M"))
			return "Test Method - Microbiologic";
		else if (documentNumber.startsWith("94.M"))
			return "Test Method - Microbiologic";
		else if (documentNumber.startsWith("90.STM"))
			return "Test Method - Microbiologic";

		else if (documentNumber.startsWith("90.P"))
			return "Test Method - Physical";
		else if (documentNumber.startsWith("94.P"))
			return "Test Method - Physical";
		else if (documentNumber.startsWith("90.STP"))
			return "Test Method - Physical";
		else if (documentNumber.startsWith("TM-PHY-HCR-XXXX"))
			return "Test Method - Physical";
		else if (documentNumber.startsWith("90.PTM-056"))
			return "Test Method - Physical";

		return "";
	}
}
