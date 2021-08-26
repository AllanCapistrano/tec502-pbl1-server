package models;

import java.io.Serializable;
import utils.IdGenerate;

/**
 * Classe do paciente.
 *
 * @author Allan Capistrano
 */
public class Patient implements Serializable {

    private String name;
    private final String medicalRecordNumber;
    private float bodyTemperatureSensor;
    private float respiratoryFrequencySensor;
    private float oxygenationOfTheBlood;

    /**
     * Método construtor.
     *
     * @param name String - Nome do paciente.
     * @param bodyTemperatureSensor float - Valor da temperatura corporal
     * registrada pelo sensor.
     * @param respiratoryFrequencySensor float - Valor da frequência
     * respiratória registrada pelo sensor.
     * @param oxygenationOfTheBlood float - Nível do oxigênio no sangue
     * registrado pelo sensor.
     * @param id IdGenerate - Gerador de id.
     */
    public Patient(
            String name,
            float bodyTemperatureSensor,
            float respiratoryFrequencySensor,
            float oxygenationOfTheBlood,
            IdGenerate id
    ) {
        this.name = name;
        this.bodyTemperatureSensor = bodyTemperatureSensor;
        this.respiratoryFrequencySensor = respiratoryFrequencySensor;
        this.oxygenationOfTheBlood = oxygenationOfTheBlood;
        this.medicalRecordNumber = id.generate();
    }

    /**
     * Método construtor.
     *
     * @param name String - Nome do paciente.
     * @param id IdGenerate - Gerador de id.
     */
    public Patient(
            String name,
            IdGenerate id
    ) {
        this.name = name;
        this.medicalRecordNumber = id.generate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    public float getBodyTemperatureSensor() {
        return bodyTemperatureSensor;
    }

    public void setBodyTemperatureSensor(float bodyTemperatureSensor) {
        this.bodyTemperatureSensor = bodyTemperatureSensor;
    }

    public float getRespiratoryFrequencySensor() {
        return respiratoryFrequencySensor;
    }

    public void setRespiratoryFrequencySensor(float respiratoryFrequencySensor) {
        this.respiratoryFrequencySensor = respiratoryFrequencySensor;
    }

    public float getOxygenationOfTheBlood() {
        return oxygenationOfTheBlood;
    }

    public void setOxygenationOfTheBlood(float oxygenationOfTheBlood) {
        this.oxygenationOfTheBlood = oxygenationOfTheBlood;
    }

}
