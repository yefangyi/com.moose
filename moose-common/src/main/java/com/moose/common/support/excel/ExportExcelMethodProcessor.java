package com.moose.common.support.excel;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.moose.common.util.CollectionUtils;
import com.moose.common.util.EntityAccessor;
import com.moose.common.util.ExcelUtils;
import com.moose.common.util.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

public class ExportExcelMethodProcessor extends AbstractMessageConverterMethodProcessor {

    private static final String EXCEL_NAME_TEMPLATE = "attachment; filename=%s.xlsx";
    private static final MediaType MEDIA_TYPE = MediaType.parseMediaType("application/ms-excel;charset=UTF-8");
    private static final String HEAD_NAME = "Content-Disposition";

    public ExportExcelMethodProcessor() {
        super(Lists.newArrayList(new ByteArrayHttpMessageConverter()));
    }

    public ExportExcelMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    public ExportExcelMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager) {
        super(converters, manager);
    }

    public ExportExcelMethodProcessor(List<HttpMessageConverter<?>> converters, List<Object> requestResponseBodyAdvice) {
        super(converters, null, requestResponseBodyAdvice);
    }

    public ExportExcelMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice) {
        super(converters, manager, requestResponseBodyAdvice);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(ExportExcel.class) != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);

        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);

        List<Map<String, Object>> formList = null;
        if(returnValue instanceof Collection) {
            Collection<?> dataList = (Collection<?>)returnValue;
            formList = CollectionUtils.mapping(dataList, object -> {
                if(object instanceof Map) {
                    return (Map)object;
                } else if(object instanceof Collection) {
                    throw new UnknownFormatConversionException("not support Collection");
                } else {
                    return EntityAccessor.toMap(object);
                }
            });
        } else {
            formList = Lists.newArrayList(EntityAccessor.toMap(returnValue));
        }

        ExportExcel annotation = returnType.getMethodAnnotation(ExportExcel.class);
        outputMessage.getHeaders().setContentType(MEDIA_TYPE);
        outputMessage.getHeaders().set(HEAD_NAME, createHeaderValue(annotation.name()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ExcelUtils.exportExecl(outputStream, annotation.sheetName(), getMapperHeader(annotation), formList);

        // Try even with null body. ResponseBodyAdvice could get involved.
        writeWithMessageConverters(outputStream.toByteArray(), returnType, inputMessage, outputMessage);
        // Ensure headers are flushed even if no body was written.
        outputMessage.flush();
    }

    private Map<String, String> getMapperHeader(ExportExcel annotation) {
        String separator = annotation.separator();
        return Arrays.stream(annotation.mapper())
                .filter(StringUtils::isNotEmpty)
                .map(str -> {
                    List<String> strList = Splitter.on(separator).splitToList(str);
                    if(strList.size() == 1) {
                        strList.add(strList.get(0));
                    }
                    return strList;
                })
                .collect(Collectors.toMap(list -> list.get(0), list -> list.get(1)));
    }

    private String createHeaderValue(String excelName) throws UnsupportedEncodingException {
        return String.format(EXCEL_NAME_TEMPLATE, new String(excelName.getBytes("gb2312"), "ISO8859-1" ));
    }

}
