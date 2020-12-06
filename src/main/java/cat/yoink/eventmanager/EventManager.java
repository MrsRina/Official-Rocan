package cat.yoink.eventmanager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public final class EventManager
{
    public final Map<Class<?>, List<Entry<Method, Object>>> subscribers = new HashMap<>();

    public <E> E dispatch(final E event)
    {
        if (subscribers.containsKey(event.getClass()))
        {
            final List<Entry<Method, Object>> list = subscribers.get(event.getClass());
            for (int i = 0, getSize = list.size(); i < getSize; i++)
            {
                try { list.get(i).getKey().invoke(list.get(i).getValue(), event); }
                catch (final IllegalAccessException | InvocationTargetException e) { e.printStackTrace(); }
            }
        }

        return event;
    }

    public void register(final Object instance)
    {
        for (final Method method : Arrays.stream(instance.getClass().getMethods()).filter(method -> method.isAnnotationPresent(Listener.class)).sorted(Comparator.comparing(method -> -method.getAnnotation(Listener.class).value())).collect(Collectors.toList()))
        {
            final Class<?> type = method.getParameterTypes()[0];
            if (subscribers.containsKey(type)) subscribers.get(type).add(new SimpleEntry<>(method, instance));
            else subscribers.put(type, new ArrayList<>(Collections.singletonList(new SimpleEntry<>(method, instance))));
        }
    }

    public void remove(final Object instance)
    {
        for (final Method method : Arrays.stream(instance.getClass().getMethods()).filter(method -> method.isAnnotationPresent(Listener.class)).sorted(Comparator.comparing(method -> -method.getAnnotation(Listener.class).value())).collect(Collectors.toList()))
        {
            final Class<?> type = method.getParameterTypes()[0];
            if (subscribers.containsKey(type))
            {
                final List<Entry<Method, Object>> entries = subscribers.get(type);
                for (int i = 0; i < entries.size(); i++)
                {
                    if (entries.get(i).getValue().equals(instance))
                    {
                        if (subscribers.get(type).size() == 1) subscribers.remove(type);
                        else subscribers.get(type).remove(entries.get(i));
                    }
                }
            }
        }
    }
}
