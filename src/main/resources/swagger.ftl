swagger: '2.0'
info:
  description: ${fixInfo.apiDesc}
  version: '1.0'
  title: ${fixInfo.apiName}
  contact: {}
schemes: ['http']
host: '${fixInfo.providerHost4Test}'
basePath: ${fixInfo.eopBasePath}
tags:
  - name: ${fixInfo.serverName}-open-api-controller
    description: ${fixInfo.serverName} Open Api Controller
paths:
  ${fixInfo.resourcePath}:
<#if fixInfo.httpMethod=="POST">
    post:
      tags:
        - ${fixInfo.serverName}-open-api-controller
      summary: ${fixInfo.apiDesc}
      description: ${fixInfo.apiDesc}
      operationId: create${fixInfo.resourcePath?substring(1)}UsingPOST
      consumes:
        - application/json
      produces:
        - '*/*'
      parameters:
        - in: body
          name: req
          description: req
          required: true
          schema:
           <#list reqList as req>
               <#if req.parentOrderNo=="">
            $ref: '#/definitions/${req.propertyName}DTO'
               </#if>
           </#list>
  <#elseif fixInfo.httpMethod=="GET">
    get:
      tags:
        - ${fixInfo.serverName}-open-api-controller
      summary: ${fixInfo.apiDesc}
        - '*/*'
      parameters:
    <#list reqList as req>
        - name: ${req.propertyName}
          in: query
          description: ${req.desc}
          required: ${req.required?c}
          type: string
    </#list>
</#if>
      responses:
        '200':
          description: OK
          schema:
            <#list respList as resp>
              <#if resp.parentOrderNo=="">
            $ref: '#/definitions/${resp.propertyName}DTO'
              </#if>
            </#list>
        '201':
          description: Created
        '401':
          description: Unauthorized
        '403':
          description: Forbidden
        '404':
          description: Not Found
      deprecated: false

definitions:
<#if fixInfo.httpMethod=="POST">
<#list reqList as req>
  <#if !req.leafNode>
  ${req.propertyName}DTO:
    type: object
    properties:
  <#list reqList as subReq>
    <#if subReq.parentOrderNo==req.orderNo>
      <#if !subReq.leafNode>
      ${subReq.propertyName}:
        description: ${subReq.desc}
        $ref: '#/definitions/${subReq.propertyName}DTO'
      <#elseif subReq.leafNode >
      ${subReq.propertyName}:
        type: string
        description: ${subReq.desc}
      </#if>
      </#if>
  </#list>
    title: ${req.propertyName}DTO
    <#elseif req.leafNode&&req.parentOrderNo=="">
  ${req.propertyName}DTO:
    type: object
    properties:
      ${req.propertyName}:
        type: string
        description: ${req.desc}
  </#if>
</#list>
</#if>

<#list respList as resp>
  <#if !resp.leafNode>
  ${resp.propertyName}DTO:
    type: object
    properties:
    <#list respList as subResp>
      <#if subResp.parentOrderNo==resp.orderNo>
        <#if !subResp.leafNode>
      ${subResp.propertyName}:
        description: ${subResp.desc}
        $ref: '#/definitions/${subResp.propertyName}DTO'
        <#elseif subResp.leafNode >
      ${subResp.propertyName}:
        type: string
        description: ${subResp.desc}
        </#if>
      </#if>
    </#list>
    title: ${resp.propertyName}DTO
  <#elseif resp.leafNode&&resp.parentOrderNo=="">
  ${resp.propertyName}DTO:
    type: object
    properties:
      ${resp.propertyName}:
        type: string
        description: ${resp.desc}
  </#if>
</#list>