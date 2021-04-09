package me.batizhao.ims.unit.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author batizhao
 * @since 2020-02-07
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Tag("unit")
public abstract class BaseServiceUnitTest {
}
