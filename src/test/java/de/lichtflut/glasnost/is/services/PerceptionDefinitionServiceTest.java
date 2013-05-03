/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.lichtflut.glasnost.is.services;

import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.model.logic.PerceptionItem;
import de.lichtflut.rb.core.services.ArastrejuResourceFactory;
import de.lichtflut.rb.core.services.ServiceContext;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.naming.QualifiedName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * <p>
 *  Test case for perception definition service.
 * </p>
 * <p>
 *  Created 02.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
@RunWith(MockitoJUnitRunner.class)
public class PerceptionDefinitionServiceTest {

    private final QualifiedName CPT = QualifiedName.fromURI("http://lf#ConceptPerception");

    private final QualifiedName DEV = QualifiedName.fromURI("http://lf#DevPerception");

    // ----------------------------------------------------

    @Mock
    private ServiceContext context;

    @Mock
    private ArastrejuResourceFactory arasFactory;

    @Mock
    private Conversation conversation;

    // ----------------------------------------------------

    private PerceptionDefinitionService service;

    // ----------------------------------------------------

    @Before
    public void setUp() {
        service = new PerceptionDefinitionServiceImpl(context, arasFactory);
        when(arasFactory.getConversation()).thenReturn(conversation);
    }

    // ----------------------------------------------------

    @Test
    public void shouldFindExistingPerception() {
        givenCptPerceptionExists();

        Perception perception = service.findByQualifiedName(CPT);
        assertNotNull(perception);
    }

    @Test
    public void shouldReturnNullWhenPerceptionDoesNotExist() {
        givenNoPerceptionExists();

        Perception perception = service.findByQualifiedName(CPT);
        assertNull(perception);
    }

    @Test
    public void shouldClonePerception() {
        givenCptPerceptionExists();

        Perception dev = service.cloneItems(DEV, CPT);
        assertNotNull(dev);

        assertEquals(CPT, dev.getBasePerception().getQualifiedName());

        List<PerceptionItem> items = dev.getItems();
        assertEquals(4, items.size());

        PerceptionItem myServer1 = findItemByID(dev, "MyServer1");
        assertNotNull(myServer1);
        assertEquals(DEV, myServer1.getPerception().getQualifiedName());

        List<PerceptionItem> subItems = myServer1.getSubItems();
        assertEquals(1, subItems.size());
        PerceptionItem singleSubItem = subItems.get(0);
        assertEquals(DEV, singleSubItem.getPerception().getQualifiedName());

        List<PerceptionItem> roots = dev.getTreeRootItems();
        assertEquals(1, roots.size());
    }

    // -- GIVEN -------------------------------------------

    private Perception givenCptPerceptionExists() {
        Perception perception = new Perception(CPT);
        perception.setID("Concept");

        PerceptionItem root = new PerceptionItem();
        root.setID("MyDataCenter");
        root.setName("MyDataCenter");
        root.setPerception(perception);

        PerceptionItem server1 = new PerceptionItem();
        server1.setID("MyServer1");
        server1.setName("MyServer1");
        server1.setPerception(perception);

        PerceptionItem server2 = new PerceptionItem();
        server2.setID("MyServer2");
        server2.setName("MyServer2");
        server2.setPerception(perception);

        PerceptionItem app1 = new PerceptionItem();
        app1.setID("MyApp1");
        app1.setName("MyApp1");
        app1.setPerception(perception);

        root.addSubItem(server1);
        root.addSubItem(server2);
        server1.addSubItem(app1);

        perception.addTreeRootItem(root);

        perception.addItem(root);
        perception.addItem(server1);
        perception.addItem(server2);
        perception.addItem(app1);

        when(conversation.findResource(CPT)).thenReturn(perception);
        return perception;
    }

    private void givenNoPerceptionExists() {
        when(conversation.findResource(CPT)).thenReturn(null);
    }

    // -- UTILS -------------------------------------------

    private PerceptionItem findItemByID(Perception perception, String id) {
        for (PerceptionItem item : perception.getItems()) {
            if (id.equals(item.getID())) {
                return item;
            }
        }
        return null;
    }

}
