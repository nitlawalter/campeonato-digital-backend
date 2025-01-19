module com.torneios {
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.web;
    requires spring.core;
    requires spring.beans;
    requires spring.data.jpa;
    requires lombok;
    requires jakarta.validation;
    requires spring.websocket;
    requires spring.messaging;
    requires io.swagger.v3.oas.annotations;
    requires spring.security.core;
    requires spring.security.web;
    requires spring.security.config;
    requires jjwt.api;
    requires spring.tx;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires io.swagger.v3.oas.models;
    requires spring.security.crypto;
    requires org.apache.tomcat.embed.core;
    requires jakarta.persistence;

    exports com.torneios;
    exports com.torneios.config;
    exports com.torneios.controller;
    exports com.torneios.dto;
    exports com.torneios.exception;
    exports com.torneios.model;
    exports com.torneios.model.enums;
    exports com.torneios.repository;
    exports com.torneios.service;
    exports com.torneios.service.impl;
} 