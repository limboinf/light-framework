package com.limbo.light.web.test;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.SelectPackages;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Disabled
@ExtendWith(SpringExtension.class)
@SelectPackages("com.limbo.light.web.test.cases")
public class SuiteTest {
}
