package models;

import java.io.Serializable;

/**
 * Classe do paciente.
 *
 * @author Allan Capistrano
 */
public class Patient implements Serializable {

    private String name;
    private final String medicalRecordNumber;
    private float bodyTemperature;
    private float respiratoryFrequency;
    private float bloodOxygenation;
    private float heartRate;

    /**
     * Método construtor.
     *
     * @param name String - Nome do paciente.
     * @param bodyTemperature float - Valor da temperatura corporal registrada 
     * pelo sensor.
     * @param respiratoryFrequency float - Valor da frequência respiratória 
     * registrada pelo sensor.
     * @param bloodOxygenation float - Nível de oxigênio no sangue registrado 
     * pelo sensor.
     * @param heartRate float - Frequência cardíaca registrada pelo sensor.
     * @param medicalRecordNumber String - Número da ficha médica do paciente.
     */
    public Patient(
            String name,
            float bodyTemperature,
            float respiratoryFrequency,
            float bloodOxygenation,
            float heartRate,
            String medicalRecordNumber
    ) {
        this.name = name;
        this.bodyTemperature = bodyTemperature;
        this.respiratoryFrequency = respiratoryFrequency;
        this.bloodOxygenation = bloodOxygenation;
        this.heartRate = heartRate;
        this.medicalRecordNumber = medicalRecordNumber;
    }

    /**
     * Método construtor.
     *
     * @param name String - Nome do paciente.
     * @param medicalRecordNumber String - Número da ficha médica do paciente.
     */
    public Patient(
            String name,
            String medicalRecordNumber
    ) {
        this.name = name;
        this.medicalRecordNumber = medicalRecordNumber;
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

    public float getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(float bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public float getRespiratoryFrequency() {
        return respiratoryFrequency;
    }

    public void setRespiratoryFrequency(float respiratoryFrequency) {
        this.respiratoryFrequency = respiratoryFrequency;
    }

    public float getBloodOxygenation() {
        return bloodOxygenation;
    }

    public void setBloodOxygenation(float bloodOxygenation) {
        this.bloodOxygenation = bloodOxygenation;
    }

    public float getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(float heartRate) {
        this.heartRate = heartRate;
    }

}
