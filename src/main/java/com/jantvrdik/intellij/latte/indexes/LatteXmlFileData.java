package com.jantvrdik.intellij.latte.indexes;

import com.jantvrdik.intellij.latte.config.LatteFileConfiguration;
import com.jantvrdik.intellij.latte.settings.LatteFunctionSettings;
import com.jantvrdik.intellij.latte.settings.LatteTagSettings;
import com.jantvrdik.intellij.latte.settings.LatteFilterSettings;
import com.jantvrdik.intellij.latte.settings.LatteVariableSettings;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LatteXmlFileData implements Serializable {
    final private LatteFileConfiguration.VendorResult vendorResult;

    private Map<String, LatteTagSettings> tags = new HashMap<>();
    private Map<String, LatteFilterSettings> filters = new HashMap<>();
    private Map<String, LatteVariableSettings> variables = new HashMap<>();
    private Map<String, LatteFunctionSettings> functions = new HashMap<>();

    public LatteXmlFileData(LatteFileConfiguration.VendorResult vendorResult) {
        this.vendorResult = vendorResult;
    }

    public LatteFileConfiguration.VendorResult getVendorResult() {
        return vendorResult;
    }

    public void removeTag(String name) {
        tags.remove(name);
    }

    public void addTag(LatteTagSettings customMacroSettings) {
        removeTag(customMacroSettings.getMacroName());

        tags.put(customMacroSettings.getMacroName(), customMacroSettings);
    }

    public Map<String, LatteTagSettings> getTags() {
        tags.values().forEach(tag -> {
            tag.setVendor(vendorResult.vendor);
            tag.setVendorName(vendorResult.vendorName);
        });
        return Collections.unmodifiableMap(tags);
    }

    public void removeFilter(String name) {
        filters.remove(name);
    }

    public void addFilter(LatteFilterSettings filter) {
        removeFilter(filter.getModifierName());
        filters.put(filter.getModifierName(), filter);
    }

    public Map<String, LatteFilterSettings> getFilters() {
        tags.values().forEach(filter -> {
            filter.setVendor(vendorResult.vendor);
            filter.setVendorName(vendorResult.vendorName);
        });
        return Collections.unmodifiableMap(filters);
    }

    public void removeVariable(String name) {
        variables.remove(name);
    }

    public void addVariable(LatteVariableSettings variable) {
        removeVariable(variable.getVarName());
        variables.put(variable.getVarName(), variable);
    }

    public Map<String, LatteVariableSettings> getVariables() {
        variables.values().forEach(variable -> {
            variable.setVendor(vendorResult.vendor);
            variable.setVendorName(vendorResult.vendorName);
        });
        return Collections.unmodifiableMap(variables);
    }

    public void removeFunction(String name) {
        functions.remove(name);
    }

    public void addFunction(LatteFunctionSettings function) {
        removeFunction(function.getFunctionName());
        functions.put(function.getFunctionName(), function);
    }

    public Map<String, LatteFunctionSettings> getFunctions() {
        functions.values().forEach(function -> {
            function.setVendor(vendorResult.vendor);
            function.setVendorName(vendorResult.vendorName);
        });
        return Collections.unmodifiableMap(functions);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.vendorResult.vendor.toString())
                .append(this.vendorResult.vendorName)
                .append(this.tags)
                .append(this.filters)
                .append(this.variables)
                .append(this.functions)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LatteXmlFileData &&
                Objects.equals(((LatteXmlFileData) obj).getVendorResult().vendor, this.vendorResult.vendor) &&
                Objects.equals(((LatteXmlFileData) obj).getVendorResult().vendorName, this.vendorResult.vendorName);
    }
}