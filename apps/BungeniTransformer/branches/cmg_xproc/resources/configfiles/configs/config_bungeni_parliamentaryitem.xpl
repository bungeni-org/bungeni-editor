<?xml version="1.0" encoding="UTF-8"?>
<p:pipeline xmlns:p="http://www.w3.org/ns/xproc" xmlns:c="http://www.w3.org/ns/xproc-step" xmlns:cc="http://xmlcalabash.com/ns/configuration" name="config_bungeni_parliamentaryitem" version="1.0">

    <!-- p:html-parser value="tagsoup"/ -->

    <p:xslt name="groupingLevel1-common">
        <p:input port="stylesheet">
                <p:document href="D:/BungeniProject/BungeniCalabash/resources/pipeline_xslt/bungeni/parliamentaryitem/1_grouping.xsl"/>
        </p:input>
        <p:with-param name="country-code">
            <p:inline><parameter name="country-code" value="ke" /></p:inline>
        </p:with-param>
        <p:with-param name="parliament-id">
            <p:inline><parameter name="parliament-id" /></p:inline>
        </p:with-param>
        <p:with-param name="parliament-election-date">
            <p:inline><parameter name="parliament-election-date"/></p:inline>
        </p:with-param>
        <p:with-param name="for-parliament">
            <p:inline><parameter name="for-parliament"/></p:inline>
        </p:with-param>
        <p:with-param name="type-mappings">
        <p:inline>
            <parameter>
                <value>
                    <map from="question" uri-name="Question" element-name="question" />
                    <map from="bill" uri-name="Bill" element-name="bill" />
                    <map from="agenda_item" uri-name="AgendaItem" element-name="agendaItem" />
                    <map from="motion" uri-name="Motion" element-name="motion"  />
                    <map from="tabled_document" uri-name="TabledDocument" element-name="tabledDocument"  />
                    <map from="event" uri-name="Event" element-name="event" />
                    <map from="ministry" uri-name="Ministry"  element-name="ministry" />
                    <map from="user" uri-name="Person" element-name="person" />
                    <map from="member_of_parliament" uri-name="ParliamentMember" element-name="parliamentmember" />
                </value>
            </parameter>
        </p:inline>
        </p:with-param>
    </p:xslt>
    <p:xslt name="groupingLevel2-common">
        <p:input port="stylesheet">
                <p:document href="D:/BungeniProject/BungeniCalabash/resources/pipeline_xslt/bungeni/parliamentaryitem/2_ontology.xsl"/>
        </p:input>
    </p:xslt>
    <p:xslt name="groupingLevel3-common">
        <p:input port="stylesheet">
                <p:document href="D:/BungeniProject/BungeniCalabash/resources/pipeline_xslt/bungeni/parliamentaryitem/3_idgenerate.xsl"/>
        </p:input>
    </p:xslt>
    <p:xslt name="appendBungeniNS">
        <p:input port="stylesheet">
                <p:document href="D:/BungeniProject/BungeniCalabash/resources/pipeline_xslt/bungeni/common/add_bungeniportal_ns.xsl"/>
        </p:input>
    </p:xslt>

</p:pipeline>