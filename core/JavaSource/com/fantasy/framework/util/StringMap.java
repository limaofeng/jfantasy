package com.fantasy.framework.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 该类逻辑有待验证
 * @param <V>
 */
@Deprecated
public class StringMap<V> extends AbstractMap<String, V> implements Externalizable {
	protected int _width = 17;
	protected Node<V> _root = new Node<V>();
	protected boolean _ignoreCase = false;
	protected NullEntry _nullEntry = null;
	protected V _nullValue = null;
	protected HashSet<Map.Entry<String, V>> _entrySet = new HashSet<Map.Entry<String, V>>(3);
	protected Set<Map.Entry<String, V>> _umEntrySet = Collections.unmodifiableSet(this._entrySet);

	public StringMap() {
	}

	public StringMap(boolean ignoreCase) {
		this();
		this._ignoreCase = ignoreCase;
	}

	public StringMap(boolean ignoreCase, int width) {
		this();
		this._ignoreCase = ignoreCase;
		this._width = width;
	}

	public void setIgnoreCase(boolean ic) {
		if (this._root._children != null){
            throw new IllegalStateException("Must be set before first put");
        }
		this._ignoreCase = ic;
	}

	public boolean isIgnoreCase() {
		return this._ignoreCase;
	}

	public void setWidth(int width) {
		this._width = width;
	}

	public int getWidth() {
		return this._width;
	}

	@SuppressWarnings("unchecked")
	public V put(String key, V value) {
		if (key == null) {
			V oldValue = this._nullValue;
			this._nullValue = value;
			if (this._nullEntry == null) {
				this._nullEntry = new NullEntry();
				this._entrySet.add(this._nullEntry);
			}
			return oldValue;
		}

		Node<V> node = this._root;
		int ni = -1;
		Node<V> prev = null;
		Node<V> parent = null;

		for (int i = 0; i < key.length();) {
			char c = key.charAt(i);

			if (ni == -1) {
				parent = node;
				prev = null;
				ni = 0;
				node = node._children == null ? null : node._children[c % this._width];
			}

			while (node != null) {
				if ((node._char[ni] == c) || ((this._ignoreCase) && (node._ochar[ni] == c))) {
					prev = null;
					ni++;
					if (ni != node._char.length){
                        break;
                    }
					ni = -1;
					break;
				}
				if (ni == 0) {
					prev = node;
					node = node._next;
					continue;
				}
				node.split(this, ni);
				i--;
				ni = -1;
				break;
			}

			node = new Node<V>(this._ignoreCase, key, i);

			if (prev != null) {
				prev._next = node;
				break;
			}
			if (parent != null) {
				if (parent._children == null){
                    parent._children = new Node[this._width];
                }
				parent._children[c % this._width] = node;
				int oi = node._ochar != null ? node._ochar[0] % this._width : 0;
				if ((node._ochar != null) && (node._char[0] % this._width != oi)) {
					if (parent._children[oi] == null) {
						parent._children[oi] = node;
					} else {
						Node<V> n = parent._children[oi];
						while (n._next != null){
                            n = n._next;
                        }
						n._next = node;
					}
				}
				break;
			}
			this._root = node;
			break;
		}

		if (node != null) {
			if (ni > 0) {
				node.split(this, ni);
			}
			V old = node._value;
			node._key = key;
			node._value = value;
			this._entrySet.add(node);
			return old;
		}
		return null;
	}

	public V get(Object key) {
		if (key == null){
            return this._nullValue;
        }
		if (key instanceof String){
            return get((String) key);
        }
		return get(key.toString());
	}

	public V get(String key) {
		if (key == null) {
			return this._nullValue;
		}
		Map.Entry<String, V> entry = getEntry(key, 0, key.length());
		if (entry == null){
            return null;
        }
		return entry.getValue();
	}

	public Map.Entry<String, V> getEntry(String key, int offset, int length) {
		if (key == null) {
			return this._nullEntry;
		}
		Node<V> node = this._root;
		int ni = -1;

		for (int i = 0; i < length; i++) {
			char c = key.charAt(offset + i);

			if (ni == -1) {
				ni = 0;
				node = node._children == null ? null : node._children[c % this._width];
			}
			while (true) {
				if (node != null) {
					if ((node._char[ni] == c) || ((this._ignoreCase) && (node._ochar[ni] == c))) {
						ni++;
						if (ni != node._char.length){
                            break;
                        }
						ni = -1;
					} else {
						if (ni > 0){
                            return null;
                        }
						node = node._next;
						continue;
					}
				} else{
                    return null;
                }

			}
		}
		if (ni > 0){
            return null;
        }
		if ((node != null) && (node._key == null)){
            return null;
        }
		return node;
	}

	public Map.Entry<String, V> getEntry(char[] key, int offset, int length) {
		if (key == null) {
			return this._nullEntry;
		}
		Node<V> node = this._root;
		int ni = -1;

		for (int i = 0; i < length; i++) {
			char c = key[offset + i];

			if (ni == -1) {
				ni = 0;
				node = node._children == null ? null : node._children[c % this._width];
			}
			while (true) {
				if (node != null) {
					if ((node._char[ni] == c) || ((this._ignoreCase) && (node._ochar[ni] == c))) {
						ni++;
						if (ni != node._char.length){
                            break;
                        }
						ni = -1;
					} else {
						if (ni > 0){
                            return null;
                        }
						node = node._next;
						continue;
					}
				} else{
                    return null;
                }
			}
		}
		if (ni > 0){
            return null;
        }

		if ((node != null) && (node._key == null)){
            return null;
        }

		return node;
	}

	public Map.Entry<String, V> getBestEntry(byte[] key, int offset, int maxLength) {
		if (key == null) {
			return this._nullEntry;
		}
		Node<V> node = this._root;
		int ni = -1;

		for (int i = 0; i < maxLength; i++) {
			char c = (char) key[offset + i];

			if (ni == -1) {
				ni = 0;

				Node<V> child = node._children == null ? null : node._children[c % this._width];

				if ((child == null) && (i > 0)){
                    return node;
                }
				node = child;
			}
			while (true) {
				if (node != null) {
					if ((node._char[ni] == c) || ((this._ignoreCase) && (node._ochar[ni] == c))) {
						ni++;
						if (ni != node._char.length){
                            break;
                        }
						ni = -1;
					} else {
						if (ni > 0){
                            return null;
                        }
						node = node._next;
						continue;
					}
				} else{
                    return null;
                }

			}
		}
		if (ni > 0){
            return null;
        }

		if ((node != null) && (node._key == null)){
            return null;
        }

		return node;
	}

	public V remove(String key) {
		if (key == null) {
			V oldValue = this._nullValue;
			if (this._nullEntry != null) {
				this._entrySet.remove(this._nullEntry);
				this._nullEntry = null;
				this._nullValue = null;
			}
			return oldValue;
		}

		Node<V> node = this._root;
		int ni = -1;

		for (int i = 0; i < key.length(); i++) {
			char c = key.charAt(i);

			if (ni == -1) {
				ni = 0;
				node = node._children == null ? null : node._children[c % this._width];
			}
			while (true) {
				if (node != null) {
					if ((node._char[ni] == c) || ((this._ignoreCase) && (node._ochar[ni] == c))) {
						ni++;
						if (ni != node._char.length){
                            break;
                        }
						ni = -1;
					} else {
						if (ni > 0){
                            return null;
                        }
						node = node._next;
						continue;
					}
				} else{
                    return null;
                }

			}
		}
		if (ni > 0){
            return null;
        }

		if ((node != null) && (node._key == null)) {
			return null;
		}
		V old = node != null ? node._value : null;
		this._entrySet.remove(node);
		if (node != null) {
			node._value = null;
			node._key = null;
		}
		return old;
	}

	public Set<Map.Entry<String, V>> entrySet() {
		return this._umEntrySet;
	}

	public int size() {
		return this._entrySet.size();
	}

	public boolean isEmpty() {
		return this._entrySet.isEmpty();
	}

	public boolean containsKey(Object key) {
		if (key == null){
            return this._nullEntry != null;
        }
		return getEntry(key.toString(), 0, key == null ? 0 : key.toString().length()) != null;
	}

	public void clear() {
		this._root = new Node<V>();
		this._nullEntry = null;
		this._nullValue = null;
		this._entrySet.clear();
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		HashMap<String, V> map = new HashMap<String, V>(this);
		out.writeBoolean(this._ignoreCase);
		out.writeObject(map);
	}

	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		boolean ic = in.readBoolean();
		HashMap<String, V> map = (HashMap<String, V>) in.readObject();
		setIgnoreCase(ic);
		putAll(map);
	}

	private class NullEntry implements Map.Entry<String, V> {
		private NullEntry() {
		}

		public String getKey() {
			return null;
		}

		public V getValue() {
			return StringMap.this._nullValue;
		}

		public V setValue(V o) {
			V old = StringMap.this._nullValue;
			StringMap.this._nullValue = o;
			return old;
		}

		public String toString() {
			return "[:null=" + StringMap.this._nullValue + "]";
		}
	}

	private static class Node<V> implements Map.Entry<String, V> {
		char[] _char;
		char[] _ochar;
		Node<V> _next;
		Node<V>[] _children;
		String _key;
		V _value;

		Node() {
		}

		Node(boolean ignoreCase, String s, int offset) {
			int l = s.length() - offset;
			this._char = new char[l];
			this._ochar = new char[l];
			for (int i = 0; i < l; i++) {
				char c = s.charAt(offset + i);
				this._char[i] = c;
				if (!ignoreCase){
                    continue;
                }
				char o = c;
				if (Character.isUpperCase(c)){
                    o = Character.toLowerCase(c);
                }else if (Character.isLowerCase(c)){
                    o = Character.toUpperCase(c);
                }
				this._ochar[i] = o;
			}
		}

		@SuppressWarnings("unchecked")
		Node<V> split(StringMap<V> map, int offset) {
			Node<V> split = new Node<V>();
			int sl = this._char.length - offset;

			char[] tmp = this._char;
			this._char = new char[offset];
			split._char = new char[sl];
			System.arraycopy(tmp, 0, this._char, 0, offset);
			System.arraycopy(tmp, offset, split._char, 0, sl);

			if (this._ochar != null) {
				tmp = this._ochar;
				this._ochar = new char[offset];
				split._ochar = new char[sl];
				System.arraycopy(tmp, 0, this._ochar, 0, offset);
				System.arraycopy(tmp, offset, split._ochar, 0, sl);
			}

			split._key = this._key;
			split._value = this._value;
			this._key = null;
			this._value = null;
			if (map._entrySet.remove(this)) {
				map._entrySet.add(split);
			}
			split._children = this._children;
			this._children = new Node[map._width];
			this._children[split._char[0] % map._width] = split;
			if ((split._ochar != null) && (this._children[split._ochar[0] % map._width] != split)) {
				this._children[split._ochar[0] % map._width] = split;
			}
			return split;
		}

		public String getKey() {
			return this._key;
		}

		public V getValue() {
			return this._value;
		}

		public V setValue(V o) {
			V old = this._value;
			this._value = o;
			return old;
		}

		public String toString() {
			StringBuilder buf = new StringBuilder();
			toString(buf);
			return buf.toString();
		}

		private void toString(StringBuilder buf) {
			buf.append("{[");
			if (this._char == null){
                buf.append('-');
            }else{
                for (int i = 0; i < this._char.length; i++){
                    buf.append(this._char[i]);
                }
            }
			buf.append(':');
			buf.append(this._key);
			buf.append('=');
			buf.append(this._value);
			buf.append(']');
			if (this._children != null) {
				for (int i = 0; i < this._children.length; i++) {
					buf.append('|');
					if (this._children[i] != null) {
                        this._children[i].toString(buf);
                    }else{
                        buf.append("-");
                    }
				}
			}
			buf.append('}');
			if (this._next != null) {
				buf.append(",\n");
				this._next.toString(buf);
			}
		}
	}
}