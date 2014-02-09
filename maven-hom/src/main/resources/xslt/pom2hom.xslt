<xsl:stylesheet version="2.0" xmlns:h="http://maven.apache.org/POM/4.0.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- xpath-default-namespace="http://maven.apache.org/POM/4.0.0" -->

    <xsl:output omit-xml-declaration="yes" />


<!--     <xsl:template match="h:plugin"> -->
<!--         <xsl:element name="plugin"> -->
<!--             <xsl:attribute name="groupId"><xsl:value-of select="h:groupId" /></xsl:attribute> -->
<!--             <xsl:attribute name="artifactId"><xsl:value-of select="h:artifactId" /></xsl:attribute> -->
<!--             <xsl:attribute name="version"><xsl:value-of select="h:version" /></xsl:attribute> -->
<!--             <xsl:apply-templates select="h:executions | h:configuration" /> -->
<!--         </xsl:element> -->
<!--     </xsl:template> -->

    <xsl:template match="h:dependency">
        <xsl:element name="dependency">
            <xsl:for-each select="*">
                <xsl:attribute name="{local-name(.)}"><xsl:value-of select="." /></xsl:attribute>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

    <xsl:template match="node()">
        <xsl:copy>
            <xsl:apply-templates select="node()" />
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet> 
