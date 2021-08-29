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
    private float bodyTemperatureSensor;
    private float respiratoryFrequencySensor;
    private float bloodOxygenationSensor;

    /**
     * Método construtor.
     *
     * @param name String - Nome do paciente.
     * @param bodyTemperatureSensor float - Valor da temperatura corporal
     * registrada pelo sensor.
     * @param respiratoryFrequencySensor float - Valor da frequência
     * respiratória registrada pelo sensor.
     * @param bloodOxygenationSensor float - Nível do oxigênio no sangue
     * registrado pelo sensor.
     * @param medicalRecordNumber String - Número da ficha médica do paciente.
     */
    public Patient(
            String name,
            float bodyTemperatureSensor,
            float respiratoryFrequencySensor,
            float bloodOxygenationSensor,
            String medicalRecordNumber
    ) {
        this.name = name;
        this.bodyTemperatureSensor = bodyTemperatureSensor;
        this.respiratoryFrequencySensor = respiratoryFrequencySensor;
        this.bloodOxygenationSensor = bloodOxygenationSensor;
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

    public float getbloodOxygenationSensor() {
        return bloodOxygenationSensor;
    }

    public void setbloodOxygenationSensor(float bloodOxygenationSensor) {
        this.bloodOxygenationSensor = bloodOxygenationSensor;
    }

}
