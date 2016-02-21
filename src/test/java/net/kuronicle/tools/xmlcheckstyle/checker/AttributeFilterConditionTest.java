package net.kuronicle.tools.xmlcheckstyle.checker;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import org.junit.Test;

public class AttributeFilterConditionTest {

    /**
     * フィルタ条件と属性値が一致している場合、trueを返却する。
     */
    @Test
    public void testMatch001() {
        String attributeName = "targetAttributeName";
        String attributeValue = "hoge";
        String filterAttributeName = attributeName;
        String filterAttributeValue = attributeValue;

        Attribute attribute = mock(Attribute.class);
        when(attribute.getValue()).thenReturn(attributeValue);

        StartElement element = mock(StartElement.class);
        when(element.getAttributeByName(new QName(attributeName))).thenReturn(attribute);

        AttributeFilterCondition target = new AttributeFilterCondition(filterAttributeName, filterAttributeValue);

        boolean actual = target.match(element);
        assertThat(actual, is(true));
    }

    /**
     * フィルタ条件（正規表現）と属性値が一致している場合は、trueを返却する。
     */
    @Test
    public void testMatch002() {
        String attributeName = "targetAttributeName";
        String attributeValue = "hoge";
        String filterAttributeName = attributeName;
        String filterAttributeValue = "^ho.*";

        Attribute attribute = mock(Attribute.class);
        when(attribute.getValue()).thenReturn(attributeValue);

        StartElement element = mock(StartElement.class);
        when(element.getAttributeByName(new QName(attributeName))).thenReturn(attribute);

        AttributeFilterCondition target = new AttributeFilterCondition(filterAttributeName, filterAttributeValue);

        boolean actual = target.match(element);
        assertThat(actual, is(true));
    }

    /**
     * フィルタ条件と属性値が一致していない場合は、falseを返却する。
     */
    @Test
    public void testMatch003() {
        String attributeName = "targetAttributeName";
        String attributeValue = "hoge";
        String filterAttributeName = attributeName;
        String filterAttributeValue = "fuga";

        Attribute attribute = mock(Attribute.class);
        when(attribute.getValue()).thenReturn(attributeValue);

        StartElement element = mock(StartElement.class);
        when(element.getAttributeByName(new QName(attributeName))).thenReturn(attribute);

        AttributeFilterCondition target = new AttributeFilterCondition(filterAttributeName, filterAttributeValue);

        boolean actual = target.match(element);
        assertThat(actual, is(false));
    }

    /**
     * フィルタ条件と属性が存在していない場合は、falseを返却する。
     */
    @Test
    public void testMatch004() {
        String attributeName = "targetAttributeName";
        String filterAttributeName = attributeName;
        String filterAttributeValue = "fuga";

        StartElement element = mock(StartElement.class);
        when(element.getAttributeByName(new QName(attributeName))).thenReturn(null);

        AttributeFilterCondition target = new AttributeFilterCondition(filterAttributeName, filterAttributeValue);

        boolean actual = target.match(element);
        assertThat(actual, is(false));
    }

}
