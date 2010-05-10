<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:akn="http://www.akomantoso.org/1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output indent="yes" method="xml"/>

    <xsl:template match="/">
        <stylesheets>
            <xsl:apply-templates/>
        </stylesheets>
    </xsl:template>

    <xsl:template match="akn:*">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="text()">
       <xsl:value-of select="normalize-space(.)" />
    </xsl:template> 

	<xsl:template match="akn:akomantoso">
		<xslt step="0" name="akomantoso" href="resources/akntohtml/minixslt/akomantoso.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:act">
		<xslt step="1" name="act" href="resources/akntohtml/minixslt/act.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:bill">
		<xslt step="2" name="bill" href="resources/akntohtml/minixslt/bill.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:doc">
		<xslt step="3" name="doc" href="resources/akntohtml/minixslt/doc.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:report">
		<xslt step="4" name="report" href="resources/akntohtml/minixslt/report.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:debaterecord">
		<xslt step="5" name="debaterecord" href="resources/akntohtml/minixslt/debaterecord.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:judgment">
		<xslt step="6" name="judgment" href="resources/akntohtml/minixslt/judgment.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:preface">
		<xslt step="7" name="preface" href="resources/akntohtml/minixslt/preface.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:preamble">
		<xslt step="8" name="preamble" href="resources/akntohtml/minixslt/preamble.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:conclusions">
		<xslt step="9" name="conclusions" href="resources/akntohtml/minixslt/conclusions.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:header">
		<xslt step="10" name="header" href="resources/akntohtml/minixslt/header.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:attachments">
		<xslt step="11" name="attachments" href="resources/akntohtml/minixslt/attachments.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:attachment">
		<xslt step="12" name="attachment" href="resources/akntohtml/minixslt/attachment.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:body">
		<xslt step="13" name="body" href="resources/akntohtml/minixslt/body.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:debate">
		<xslt step="14" name="debate" href="resources/akntohtml/minixslt/debate.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:maincontent">
		<xslt step="15" name="maincontent" href="resources/akntohtml/minixslt/maincontent.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:judgmentBody">
		<xslt step="16" name="judgmentBody" href="resources/akntohtml/minixslt/judgmentBody.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:section">
		<xslt step="17" name="section" href="resources/akntohtml/minixslt/section.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:part">
		<xslt step="18" name="part" href="resources/akntohtml/minixslt/part.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:paragraph">
		<xslt step="19" name="paragraph" href="resources/akntohtml/minixslt/paragraph.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:chapter">
		<xslt step="20" name="chapter" href="resources/akntohtml/minixslt/chapter.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:title">
		<xslt step="21" name="title" href="resources/akntohtml/minixslt/title.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:book">
		<xslt step="22" name="book" href="resources/akntohtml/minixslt/book.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:tome">
		<xslt step="23" name="tome" href="resources/akntohtml/minixslt/tome.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:article">
		<xslt step="24" name="article" href="resources/akntohtml/minixslt/article.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:clause">
		<xslt step="25" name="clause" href="resources/akntohtml/minixslt/clause.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:subsection">
		<xslt step="26" name="subsection" href="resources/akntohtml/minixslt/subsection.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:subpart">
		<xslt step="27" name="subpart" href="resources/akntohtml/minixslt/subpart.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:subparagraph">
		<xslt step="28" name="subparagraph" href="resources/akntohtml/minixslt/subparagraph.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:subchapter">
		<xslt step="29" name="subchapter" href="resources/akntohtml/minixslt/subchapter.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:subtitle">
		<xslt step="30" name="subtitle" href="resources/akntohtml/minixslt/subtitle.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:subclause">
		<xslt step="31" name="subclause" href="resources/akntohtml/minixslt/subclause.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:content">
		<xslt step="32" name="content" href="resources/akntohtml/minixslt/content.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:num">
		<xslt step="33" name="num" href="resources/akntohtml/minixslt/num.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:heading">
		<xslt step="34" name="heading" href="resources/akntohtml/minixslt/heading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:subheading">
		<xslt step="35" name="subheading" href="resources/akntohtml/minixslt/subheading.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:sidenote">
		<xslt step="36" name="sidenote" href="resources/akntohtml/minixslt/sidenote.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:from">
		<xslt step="37" name="from" href="resources/akntohtml/minixslt/from.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:AdministrationOfOath">
		<xslt step="38" name="AdministrationOfOath" href="resources/akntohtml/minixslt/AdministrationOfOath.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:DeclarationOfVote">
		<xslt step="39" name="DeclarationOfVote" href="resources/akntohtml/minixslt/DeclarationOfVote.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:Communication">
		<xslt step="40" name="Communication" href="resources/akntohtml/minixslt/Communication.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:Petitions">
		<xslt step="41" name="Petitions" href="resources/akntohtml/minixslt/Petitions.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:Papers">
		<xslt step="42" name="Papers" href="resources/akntohtml/minixslt/Papers.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:NoticesOfMotion">
		<xslt step="43" name="NoticesOfMotion" href="resources/akntohtml/minixslt/NoticesOfMotion.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:Questions">
		<xslt step="44" name="Questions" href="resources/akntohtml/minixslt/Questions.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:Address">
		<xslt step="45" name="Address" href="resources/akntohtml/minixslt/Address.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ProceduralMotions">
		<xslt step="46" name="ProceduralMotions" href="resources/akntohtml/minixslt/ProceduralMotions.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:PointOfOrder">
		<xslt step="47" name="PointOfOrder" href="resources/akntohtml/minixslt/PointOfOrder.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:subdivision">
		<xslt step="48" name="subdivision" href="resources/akntohtml/minixslt/subdivision.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:speech">
		<xslt step="49" name="speech" href="resources/akntohtml/minixslt/speech.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:question">
		<xslt step="50" name="question" href="resources/akntohtml/minixslt/question.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:answer">
		<xslt step="51" name="answer" href="resources/akntohtml/minixslt/answer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:other">
		<xslt step="52" name="other" href="resources/akntohtml/minixslt/other.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:comment">
		<xslt step="53" name="comment" href="resources/akntohtml/minixslt/comment.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:list">
		<xslt step="54" name="list" href="resources/akntohtml/minixslt/list.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:introduction">
		<xslt step="55" name="introduction" href="resources/akntohtml/minixslt/introduction.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:background">
		<xslt step="56" name="background" href="resources/akntohtml/minixslt/background.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:motivation">
		<xslt step="57" name="motivation" href="resources/akntohtml/minixslt/motivation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:decision">
		<xslt step="58" name="decision" href="resources/akntohtml/minixslt/decision.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:tblock">
		<xslt step="59" name="tblock" href="resources/akntohtml/minixslt/tblock.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:item">
		<xslt step="60" name="item" href="resources/akntohtml/minixslt/item.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:toc">
		<xslt step="61" name="toc" href="resources/akntohtml/minixslt/toc.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:tocitem">
		<xslt step="62" name="tocitem" href="resources/akntohtml/minixslt/tocitem.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ActType">
		<xslt step="63" name="ActType" href="resources/akntohtml/minixslt/ActType.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ActTitle">
		<xslt step="64" name="ActTitle" href="resources/akntohtml/minixslt/ActTitle.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ActNumber">
		<xslt step="65" name="ActNumber" href="resources/akntohtml/minixslt/ActNumber.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ActProponent">
		<xslt step="66" name="ActProponent" href="resources/akntohtml/minixslt/ActProponent.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ActDate">
		<xslt step="67" name="ActDate" href="resources/akntohtml/minixslt/ActDate.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ActPurpose">
		<xslt step="68" name="ActPurpose" href="resources/akntohtml/minixslt/ActPurpose.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:judgmentType">
		<xslt step="69" name="judgmentType" href="resources/akntohtml/minixslt/judgmentType.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:judgmentTitle">
		<xslt step="70" name="judgmentTitle" href="resources/akntohtml/minixslt/judgmentTitle.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:judgmentNumber">
		<xslt step="71" name="judgmentNumber" href="resources/akntohtml/minixslt/judgmentNumber.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:courtType">
		<xslt step="72" name="courtType" href="resources/akntohtml/minixslt/courtType.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:neutralCitation">
		<xslt step="73" name="neutralCitation" href="resources/akntohtml/minixslt/neutralCitation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:party">
		<xslt step="74" name="party" href="resources/akntohtml/minixslt/party.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:judge">
		<xslt step="75" name="judge" href="resources/akntohtml/minixslt/judge.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:judgmentDate">
		<xslt step="76" name="judgmentDate" href="resources/akntohtml/minixslt/judgmentDate.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:mref">
		<xslt step="77" name="mref" href="resources/akntohtml/minixslt/mref.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ref">
		<xslt step="78" name="ref" href="resources/akntohtml/minixslt/ref.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:rref">
		<xslt step="79" name="rref" href="resources/akntohtml/minixslt/rref.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:mod">
		<xslt step="80" name="mod" href="resources/akntohtml/minixslt/mod.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:mmod">
		<xslt step="81" name="mmod" href="resources/akntohtml/minixslt/mmod.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:rmod">
		<xslt step="82" name="rmod" href="resources/akntohtml/minixslt/rmod.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:quotedText">
		<xslt step="83" name="quotedText" href="resources/akntohtml/minixslt/quotedText.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:quotedStructure">
		<xslt step="84" name="quotedStructure" href="resources/akntohtml/minixslt/quotedStructure.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:def">
		<xslt step="85" name="def" href="resources/akntohtml/minixslt/def.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ins">
		<xslt step="86" name="ins" href="resources/akntohtml/minixslt/ins.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:del">
		<xslt step="87" name="del" href="resources/akntohtml/minixslt/del.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:omissis">
		<xslt step="88" name="omissis" href="resources/akntohtml/minixslt/omissis.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:noteref">
		<xslt step="89" name="noteref" href="resources/akntohtml/minixslt/noteref.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:recordedTime">
		<xslt step="90" name="recordedTime" href="resources/akntohtml/minixslt/recordedTime.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:eol">
		<xslt step="91" name="eol" href="resources/akntohtml/minixslt/eol.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:eop">
		<xslt step="92" name="eop" href="resources/akntohtml/minixslt/eop.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:hcontainer">
		<xslt step="93" name="hcontainer" href="resources/akntohtml/minixslt/hcontainer.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:container">
		<xslt step="94" name="container" href="resources/akntohtml/minixslt/container.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:block">
		<xslt step="95" name="block" href="resources/akntohtml/minixslt/block.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:inline">
		<xslt step="96" name="inline" href="resources/akntohtml/minixslt/inline.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:marker">
		<xslt step="97" name="marker" href="resources/akntohtml/minixslt/marker.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:foreign">
		<xslt step="98" name="foreign" href="resources/akntohtml/minixslt/foreign.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:div">
		<xslt step="99" name="div" href="resources/akntohtml/minixslt/div.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:p">
		<xslt step="100" name="p" href="resources/akntohtml/minixslt/p.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:li">
		<xslt step="101" name="li" href="resources/akntohtml/minixslt/li.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:span">
		<xslt step="102" name="span" href="resources/akntohtml/minixslt/span.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:b">
		<xslt step="103" name="b" href="resources/akntohtml/minixslt/b.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:i">
		<xslt step="104" name="i" href="resources/akntohtml/minixslt/i.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:a">
		<xslt step="105" name="a" href="resources/akntohtml/minixslt/a.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:img">
		<xslt step="106" name="img" href="resources/akntohtml/minixslt/img.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ul">
		<xslt step="107" name="ul" href="resources/akntohtml/minixslt/ul.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ol">
		<xslt step="108" name="ol" href="resources/akntohtml/minixslt/ol.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:table">
		<xslt step="109" name="table" href="resources/akntohtml/minixslt/table.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:tr">
		<xslt step="110" name="tr" href="resources/akntohtml/minixslt/tr.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:th">
		<xslt step="111" name="th" href="resources/akntohtml/minixslt/th.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:td">
		<xslt step="112" name="td" href="resources/akntohtml/minixslt/td.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:meta">
		<xslt step="113" name="meta" href="resources/akntohtml/minixslt/meta.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:identification">
		<xslt step="114" name="identification" href="resources/akntohtml/minixslt/identification.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:FRBRWork">
		<xslt step="115" name="FRBRWork" href="resources/akntohtml/minixslt/FRBRWork.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:FRBRExpression">
		<xslt step="116" name="FRBRExpression" href="resources/akntohtml/minixslt/FRBRExpression.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:FRBRManifestation">
		<xslt step="117" name="FRBRManifestation" href="resources/akntohtml/minixslt/FRBRManifestation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:FRBRItem">
		<xslt step="118" name="FRBRItem" href="resources/akntohtml/minixslt/FRBRItem.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:this">
		<xslt step="119" name="this" href="resources/akntohtml/minixslt/this.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:uri">
		<xslt step="120" name="uri" href="resources/akntohtml/minixslt/uri.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:alias">
		<xslt step="121" name="alias" href="resources/akntohtml/minixslt/alias.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:date">
		<xslt step="122" name="date" href="resources/akntohtml/minixslt/date.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:author">
		<xslt step="123" name="author" href="resources/akntohtml/minixslt/author.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:components">
		<xslt step="124" name="components" href="resources/akntohtml/minixslt/components.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:component">
		<xslt step="125" name="component" href="resources/akntohtml/minixslt/component.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:preservation">
		<xslt step="126" name="preservation" href="resources/akntohtml/minixslt/preservation.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:publication">
		<xslt step="127" name="publication" href="resources/akntohtml/minixslt/publication.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:classification">
		<xslt step="128" name="classification" href="resources/akntohtml/minixslt/classification.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:keyword">
		<xslt step="129" name="keyword" href="resources/akntohtml/minixslt/keyword.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:lifecycle">
		<xslt step="130" name="lifecycle" href="resources/akntohtml/minixslt/lifecycle.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:event">
		<xslt step="131" name="event" href="resources/akntohtml/minixslt/event.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:workflow">
		<xslt step="132" name="workflow" href="resources/akntohtml/minixslt/workflow.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:action">
		<xslt step="133" name="action" href="resources/akntohtml/minixslt/action.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:analysis">
		<xslt step="134" name="analysis" href="resources/akntohtml/minixslt/analysis.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ActiveModifications">
		<xslt step="135" name="ActiveModifications" href="resources/akntohtml/minixslt/ActiveModifications.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:PassiveModifications">
		<xslt step="136" name="PassiveModifications" href="resources/akntohtml/minixslt/PassiveModifications.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:TextualMod">
		<xslt step="137" name="TextualMod" href="resources/akntohtml/minixslt/TextualMod.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:MeaningMod">
		<xslt step="138" name="MeaningMod" href="resources/akntohtml/minixslt/MeaningMod.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ScopeMod">
		<xslt step="139" name="ScopeMod" href="resources/akntohtml/minixslt/ScopeMod.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ForceMod">
		<xslt step="140" name="ForceMod" href="resources/akntohtml/minixslt/ForceMod.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:EfficacyMod">
		<xslt step="141" name="EfficacyMod" href="resources/akntohtml/minixslt/EfficacyMod.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:Legal">
		<xslt step="142" name="Legal" href="resources/akntohtml/minixslt/Legal.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:source">
		<xslt step="143" name="source" href="resources/akntohtml/minixslt/source.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:destination">
		<xslt step="144" name="destination" href="resources/akntohtml/minixslt/destination.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:force">
		<xslt step="145" name="force" href="resources/akntohtml/minixslt/force.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:efficacy">
		<xslt step="146" name="efficacy" href="resources/akntohtml/minixslt/efficacy.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:application">
		<xslt step="147" name="application" href="resources/akntohtml/minixslt/application.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:duration">
		<xslt step="148" name="duration" href="resources/akntohtml/minixslt/duration.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:condition">
		<xslt step="149" name="condition" href="resources/akntohtml/minixslt/condition.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:old">
		<xslt step="150" name="old" href="resources/akntohtml/minixslt/old.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:new">
		<xslt step="151" name="new" href="resources/akntohtml/minixslt/new.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:domain">
		<xslt step="152" name="domain" href="resources/akntohtml/minixslt/domain.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:references">
		<xslt step="153" name="references" href="resources/akntohtml/minixslt/references.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:Original">
		<xslt step="154" name="Original" href="resources/akntohtml/minixslt/Original.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:PassiveRef">
		<xslt step="155" name="PassiveRef" href="resources/akntohtml/minixslt/PassiveRef.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:ActiveRef">
		<xslt step="156" name="ActiveRef" href="resources/akntohtml/minixslt/ActiveRef.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:Jurisprudence">
		<xslt step="157" name="Jurisprudence" href="resources/akntohtml/minixslt/Jurisprudence.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:HasAttachment">
		<xslt step="158" name="HasAttachment" href="resources/akntohtml/minixslt/HasAttachment.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:AttachmentOf">
		<xslt step="159" name="AttachmentOf" href="resources/akntohtml/minixslt/AttachmentOf.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:TLCPerson">
		<xslt step="160" name="TLCPerson" href="resources/akntohtml/minixslt/TLCPerson.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:TLCOrganization">
		<xslt step="161" name="TLCOrganization" href="resources/akntohtml/minixslt/TLCOrganization.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:TLCConcept">
		<xslt step="162" name="TLCConcept" href="resources/akntohtml/minixslt/TLCConcept.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:TLCObject">
		<xslt step="163" name="TLCObject" href="resources/akntohtml/minixslt/TLCObject.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:TLCEvent">
		<xslt step="164" name="TLCEvent" href="resources/akntohtml/minixslt/TLCEvent.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:TLCPlace">
		<xslt step="165" name="TLCPlace" href="resources/akntohtml/minixslt/TLCPlace.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:TLCProcess">
		<xslt step="166" name="TLCProcess" href="resources/akntohtml/minixslt/TLCProcess.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:TLCRole">
		<xslt step="167" name="TLCRole" href="resources/akntohtml/minixslt/TLCRole.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:TLCTerm">
		<xslt step="168" name="TLCTerm" href="resources/akntohtml/minixslt/TLCTerm.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:TLCReference">
		<xslt step="169" name="TLCReference" href="resources/akntohtml/minixslt/TLCReference.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:notes">
		<xslt step="170" name="notes" href="resources/akntohtml/minixslt/notes.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:note">
		<xslt step="171" name="note" href="resources/akntohtml/minixslt/note.xsl" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="akn:proprietary">
		<xslt step="172" name="proprietary" href="resources/akntohtml/minixslt/proprietary.xsl" />
		<xsl:apply-templates />
	</xsl:template>


</xsl:stylesheet>