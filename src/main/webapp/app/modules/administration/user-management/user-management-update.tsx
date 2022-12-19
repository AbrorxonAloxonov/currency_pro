import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getUser, getRoles, updateUser, createUser, reset } from './user-management.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserManagementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { login } = useParams<'login'>();
  const isNew = login === undefined;

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getUser(login));
    }
    dispatch(getRoles());
    return () => {
      dispatch(reset());
    };
  }, [login]);

  const handleClose = () => {
    navigate('/admin/user-management');
  };

  const saveUser = values => {
    if (isNew) {
      dispatch(createUser(values));
    } else {
      dispatch(updateUser(values));
    }
    handleClose();
  };

  const isInvalid = false;
  const user = useAppSelector(state => state.userManagement.user);
  const loading = useAppSelector(state => state.userManagement.loading);
  const updating = useAppSelector(state => state.userManagement.updating);
  const authorities = useAppSelector(state => state.userManagement.authorities);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>Foydalanuvchini tahrirlash</h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm onSubmit={saveUser} defaultValues={user}>
              {user.id ? <ValidatedField type="text" name="id" required readOnly label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                type="text"
                name="login"
                label="Login"
                validate={{
                  required: {
                    value: true,
                    message: 'Login kiritilsin.',
                  },
                  pattern: {
                    value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                    message: 'Your username is invalid.',
                  },
                  minLength: {
                    value: 1,
                    message: "Login kamida 1 ta harfdan iborat bo'lsin",
                  },
                  maxLength: {
                    value: 50,
                    message: "Login ko'pi bilan 50 ta harfdan iborat bo'lsin",
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="firstName"
                label="Ismi"
                validate={{
                  maxLength: {
                    value: 50,
                    message: 'Ko`pi bilan 50ta bo`lsin.',
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="lastName"
                label="Familiyasi"
                validate={{
                  maxLength: {
                    value: 50,
                    message: 'Ko`pi bilan 50ta bo`lsin.',
                  },
                }}
              />
              <FormText>This field cannot be longer than 50 characters.</FormText>
              <ValidatedField
                name="email"
                label="Email"
                placeholder="Sizning email"
                type="email"
                validate={{
                  required: {
                    value: true,
                    message: 'Email kiritilishi zarur.',
                  },
                  minLength: {
                    value: 5,
                    message: 'Email uzunligi kamida 5 harfdan iborat bo`lsin',
                  },
                  maxLength: {
                    value: 254,
                    message: 'Email 50 harfdan uzun bo`lmasin',
                  },
                  validate: v => isEmail(v) || 'Iltimos, to`g`ri Email kiriting.',
                }}
              />
              <ValidatedField type="checkbox" name="activated" check value={true} disabled={!user.id} label="Faol" />
              <ValidatedField type="select" name="authorities" multiple label="Profili">
                {authorities.map(role => (
                  <option value={role} key={role}>
                    {role}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} to="/admin/user-management" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Orqaga</span>
              </Button>
              &nbsp;
              <Button color="primary" type="submit" disabled={isInvalid || updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Saqla
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UserManagementUpdate;
