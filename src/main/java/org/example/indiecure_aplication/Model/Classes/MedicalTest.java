package org.example.indiecure_aplication.Model.Classes;



public class MedicalTest {
    int id;
    String name;
    TestResult testResult;

    public MedicalTest() {

    }
    public MedicalTest(int id, String name, TestResult testResult) {
        this.id = id;
        this.name = name;
        this.testResult = testResult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }
}
