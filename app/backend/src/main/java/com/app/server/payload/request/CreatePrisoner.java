package com.app.server.payload.request;

import java.util.List;

import com.app.server.model.CriminalRecord;
import com.app.server.model.MedicalPrescription;
import com.app.server.model.Prisoner;

public class CreatePrisoner {

	private Prisoner prisoner;
	
	private List<CriminalRecord> criminalRecord;
	
	private List<MedicalPrescription> medicalPrescription;

	public CreatePrisoner(Prisoner prisoner, List<CriminalRecord> criminalRecord,
			List<MedicalPrescription> medicalPrescription) {
		super();
		this.prisoner = prisoner;
		this.criminalRecord = criminalRecord;
		this.medicalPrescription = medicalPrescription;
	}

	public Prisoner getPrisoner() {
		return prisoner;
	}

	public void setPrisoner(Prisoner prisoner) {
		this.prisoner = prisoner;
	}

	public List<CriminalRecord> getCriminalRecord() {
		return criminalRecord;
	}

	public void setCriminalRecord(List<CriminalRecord> criminalRecord) {
		this.criminalRecord = criminalRecord;
	}

	public List<MedicalPrescription> getMedicalPrescription() {
		return medicalPrescription;
	}

	public void setMedicalPrescription(List<MedicalPrescription> medicalPrescription) {
		this.medicalPrescription = medicalPrescription;
	}


	
	
}
