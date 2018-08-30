package org.camunda.bpm.getstarted.loanapproval;


import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class VersioningTest {

    @Test
    public void testDecisionCel() {
        RestAssured.registerParser("text/plain", Parser.JSON);
        RestAssured.defaultParser = Parser.JSON;

        given().get("load/cel.dmn");

        String response = when("run/CEL/10");

        assertEquals("result: {form=water}", response);
    }

    @Test
    public void testDecisionFar() {
        RestAssured.registerParser("text/plain", Parser.JSON);
        RestAssured.defaultParser = Parser.JSON;

        given().get("load/far.dmn");

        String response = when("run/FAR/10");

        assertEquals("result: {form=ice}", response);
    }

    @Test
    public void testSubdecisionCel() {
        RestAssured.registerParser("text/plain", Parser.JSON);
        RestAssured.defaultParser = Parser.JSON;

        given().get("load/cel.dmn");

        String response = when("run/SUB/10");

        assertEquals("result: {warm=true}", response);

    }

    @Test
    public void testSubdecisionFar() {
        RestAssured.registerParser("text/plain", Parser.JSON);
        RestAssured.defaultParser = Parser.JSON;

        given().get("load/far.dmn");

        String response = when("run/SUB/10");

        assertEquals("result: {warm=false}", response);

    }

    @Test
    public void testIndependency() {
        RestAssured.registerParser("text/plain", Parser.JSON);
        RestAssured.defaultParser = Parser.JSON;

        given().get("load/far.dmn");
        given().get("load/cel.dmn");

        String response = when("run/CEL/-1");
        assertEquals("result: {form=ice}", response);

        response = when("run/CEL/1");
        assertEquals("result: {form=water}", response);

        response = when("run/FAR/-1");
        assertEquals("result: {form=ice}", response);

        response = when("run/FAR/1");
        assertEquals("result: {form=ice}", response);

        response = when("run/SUB/1");
        assertEquals("result: {warm=true}", response);

    }

    private String when(String request){
        return RestAssured
                .when()
                .get(request)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .print();
    }

}