package net.kuronicle.tools.xmlcheckstyle.checker;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import net.kuronicle.tools.xmlcheckstyle.CheckError;
import net.kuronicle.tools.xmlcheckstyle.Severity;

import org.junit.Test;

public class AttributeValuePatternCheckerTest {

    /**
     * XMLパスと属性値が一致する場合はnullを返却する。
     */
    @Test
    public void testStartElement001() {
        String targetXmlPathRegex = "/parents/.*";
        String attributeName = "name";
        String valuePatternRegex = "val.*";
        String severity = "warning";

        String xmlPath = targetXmlPathRegex;
        String elementAttributeValue = "value";

        StartElement mockStartElement = mock(StartElement.class);
        Attribute mockAttribute = mock(Attribute.class);
        when(mockAttribute.getValue()).thenReturn(elementAttributeValue);
        when(mockStartElement.getAttributeByName(new QName(attributeName))).thenReturn(mockAttribute);

        AttributeValuePatternChecker target = new AttributeValuePatternChecker(targetXmlPathRegex, attributeName, valuePatternRegex, severity);
        CheckError actual = target.startElement(xmlPath, mockStartElement);

        assertThat(actual, is(nullValue()));
    }

    /**
     * XMLパスが一致しない場合はnullを返却する。
     */
    @Test
    public void testStartElement002() {
        String targetXmlPathRegex = "/parents/.*";
        String attributeName = "name";
        String valuePatternRegex = "val.*";
        String severity = "warning";

        String xmlPath = "/hoge";
        String elementAttributeValue = "value";

        StartElement mockStartElement = mock(StartElement.class);
        Attribute mockAttribute = mock(Attribute.class);
        when(mockAttribute.getValue()).thenReturn(elementAttributeValue);
        when(mockStartElement.getAttributeByName(new QName(attributeName))).thenReturn(mockAttribute);

        AttributeValuePatternChecker target = new AttributeValuePatternChecker(targetXmlPathRegex, attributeName, valuePatternRegex, severity);
        CheckError actual = target.startElement(xmlPath, mockStartElement);

        assertThat(actual, is(nullValue()));
    }

    /**
     * XMLパスが一致し、属性値が一致しない場合、CheckErrorを返却する。
     */
    @Test
    public void testStartElement003() {
        String targetXmlPathRegex = "/parents/child";
        String attributeName = "name";
        String valuePatternRegex = "hoge";
        String severity = "warning";

        String xmlPath = targetXmlPathRegex;
        String elementAttributeValue = "value";

        StartElement mockStartElement = mock(StartElement.class);
        Attribute mockAttribute = mock(Attribute.class);
        when(mockAttribute.getValue()).thenReturn(elementAttributeValue);
        Location mockLocation = mock(Location.class);
        when(mockLocation.getLineNumber()).thenReturn(10);
        when(mockStartElement.getAttributeByName(new QName(attributeName))).thenReturn(mockAttribute);
        when(mockStartElement.getLocation()).thenReturn(mockLocation);

        AttributeValuePatternChecker target = new AttributeValuePatternChecker(targetXmlPathRegex, attributeName, valuePatternRegex, severity);
        CheckError actual = target.startElement(xmlPath, mockStartElement);

        System.out.println(actual);
        assertThat(actual.getLine(), is(10));
        assertThat(actual.getSeverity(), is(Severity.WARNING));
        assertThat(actual.getSource(), is(AttributeValuePatternChecker.class.getName()));
    }

    /**
     * XMLパスが一致し、属性値が存在しない場合（Attributeがnull）、CheckErrorを返却する。
     */
    @Test
    public void testStartElement004() {
        String targetXmlPathRegex = "/parents/child";
        String attributeName = "name";
        String valuePatternRegex = "hoge";
        String severity = "warning";

        String xmlPath = targetXmlPathRegex;

        StartElement mockStartElement = mock(StartElement.class);
        Location mockLocation = mock(Location.class);
        when(mockLocation.getLineNumber()).thenReturn(10);
        when(mockStartElement.getAttributeByName(new QName(attributeName))).thenReturn(null);
        when(mockStartElement.getLocation()).thenReturn(mockLocation);

        AttributeValuePatternChecker target = new AttributeValuePatternChecker(targetXmlPathRegex, attributeName, valuePatternRegex, severity);
        CheckError actual = target.startElement(xmlPath, mockStartElement);

        System.out.println(actual);
        assertThat(actual.getLine(), is(10));
        assertThat(actual.getSeverity(), is(Severity.WARNING));
        assertThat(actual.getSource(), is(AttributeValuePatternChecker.class.getName()));
    }

    /**
     * XMLパスが一致し、属性フィルタが一致し、属性値が一致しない場合、CheckErrorを返却する。
     */
    @Test
    public void testStartElement005() {
        String targetXmlPathRegex = "/parents/child";
        String attributeName = "name";
        String valuePatternRegex = "hoge";
        String severity = "warning";

        // 属性フィルタ
        String filterAttributeName = "filterAttributeName";
        String filterAttributeValue = "filterAttributeValue";
        AttributeFilterCondition filter = new AttributeFilterCondition(filterAttributeName, filterAttributeValue);
        List<AttributeFilterCondition> filterList = new ArrayList<AttributeFilterCondition>();
        filterList.add(filter);

        // ターゲット
        String xmlPath = targetXmlPathRegex;
        String elementAttributeValue = "value";

        StartElement mockStartElement = mock(StartElement.class);
        Attribute mockAttribute = mock(Attribute.class);
        when(mockAttribute.getValue()).thenReturn(elementAttributeValue);
        Location mockLocation = mock(Location.class);

        Attribute mockFilterAttribute = mock(Attribute.class);
        when(mockFilterAttribute.getValue()).thenReturn(filterAttributeValue);

        when(mockLocation.getLineNumber()).thenReturn(10);
        when(mockStartElement.getAttributeByName(new QName(attributeName))).thenReturn(mockAttribute);
        when(mockStartElement.getAttributeByName(new QName(filterAttributeName))).thenReturn(mockFilterAttribute);
        when(mockStartElement.getLocation()).thenReturn(mockLocation);

        AttributeValuePatternChecker target = new AttributeValuePatternChecker(targetXmlPathRegex, attributeName, valuePatternRegex, severity);
        target.setAttributeFilterConditionList(filterList);
        CheckError actual = target.startElement(xmlPath, mockStartElement);

        System.out.println(actual);
        assertThat(actual.getLine(), is(10));
        assertThat(actual.getSeverity(), is(Severity.WARNING));
        assertThat(actual.getSource(), is(AttributeValuePatternChecker.class.getName()));
    }

    /**
     * XMLパスが一致し、属性フィルタが一致せず、属性値が一致しない場合、チェック対象でないためnullを返却する。
     */
    @Test
    public void testStartElement006() {
        String targetXmlPathRegex = "/parents/child";
        String attributeName = "name";
        String valuePatternRegex = "hoge";
        String severity = "warning";

        // 属性フィルタ
        String filterAttributeName = "filterAttributeName";
        String filterAttributeValue = "filterAttributeValue";
        AttributeFilterCondition filter = new AttributeFilterCondition(filterAttributeName, filterAttributeValue);
        List<AttributeFilterCondition> filterList = new ArrayList<AttributeFilterCondition>();
        filterList.add(filter);

        // ターゲット
        String xmlPath = targetXmlPathRegex;
        String elementAttributeValue = "value";

        StartElement mockStartElement = mock(StartElement.class);
        Attribute mockAttribute = mock(Attribute.class);
        when(mockAttribute.getValue()).thenReturn(elementAttributeValue);
        Location mockLocation = mock(Location.class);

        Attribute mockFilterAttribute = mock(Attribute.class);
        when(mockFilterAttribute.getValue()).thenReturn(filterAttributeValue + "difference");

        when(mockLocation.getLineNumber()).thenReturn(10);
        when(mockStartElement.getAttributeByName(new QName(attributeName))).thenReturn(mockAttribute);
        when(mockStartElement.getAttributeByName(new QName(filterAttributeName))).thenReturn(mockFilterAttribute);
        when(mockStartElement.getLocation()).thenReturn(mockLocation);

        AttributeValuePatternChecker target = new AttributeValuePatternChecker(targetXmlPathRegex, attributeName, valuePatternRegex, severity);
        target.setAttributeFilterConditionList(filterList);
        CheckError actual = target.startElement(xmlPath, mockStartElement);

        assertThat(actual, is(nullValue()));
    }
}
